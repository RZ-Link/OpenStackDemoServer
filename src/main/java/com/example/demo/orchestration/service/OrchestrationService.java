package com.example.demo.orchestration.service;

import cn.hutool.core.util.StrUtil;
import com.example.demo.orchestration.enums.NodeTypeEnum;
import com.example.demo.orchestration.request.Node;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.util.*;

@Service
public class OrchestrationService {

    /**
     * 解析nodes，生成template
     */
    public String createTemplate(List<Node> nodes) {
        Map<String, Object> basicData = new LinkedHashMap<>();
        // heat模板版本定义
        basicData.put("heat_template_version", "2015-04-30");
        // heat模板resources定义
        Map<String, Object> resourcesData = new LinkedHashMap<>();
        basicData.put("resources", resourcesData);

        // 解析switch
        for (Node node : nodes) {
            if (Objects.equals(node.getType(), NodeTypeEnum.SWITCH.getCode())) {
                // 创建网络
                Map<String, Object> vxlanNetwork = new LinkedHashMap<>();
                vxlanNetwork.put("type", "OS::Neutron::Net");
                vxlanNetwork.put("properties", Map.of(
                        "name", StrUtil.format("net_{}", node.getId())
                ));
                resourcesData.put(StrUtil.format("net_{}", node.getId()), vxlanNetwork);
                // 创建子网
                Map<String, Object> vxlanSubnet = new LinkedHashMap<>();
                vxlanSubnet.put("type", "OS::Neutron::Subnet");
                vxlanSubnet.put("properties", Map.of(
                        "cidr", node.getSwitchInfo().getNetworkAddress(),
                        "enable_dhcp", true,
                        "gateway_ip", node.getSwitchInfo().getGatewayIp(),
                        "name", StrUtil.format("subnet_{}", node.getId()),
                        "network", Map.of("get_resource", StrUtil.format("net_{}", node.getId())))
                );
                resourcesData.put(StrUtil.format("subnet_{}", node.getId()), vxlanSubnet);
            }
        }
        // 解析router
        for (Node node : nodes) {
            if (Objects.equals(node.getType(), NodeTypeEnum.ROUTER.getCode())) {
                // 创建路由
                Map<String, Object> router = new LinkedHashMap<>();
                router.put("type", "OS::Neutron::Router");
                router.put("properties", Map.of(
                        "name", StrUtil.format("router_{}", node.getId())
                ));
                resourcesData.put(StrUtil.format("router_{}", node.getId()), router);

                // 创建交换机端口，路由器接口
                for (int i = 0; i < node.getRouterInfo().getIpInfos().size(); i++) {
                    Node.IpInfo ipInfo = node.getRouterInfo().getIpInfos().get(i);
                    // 创建交换机端口
                    Map<String, Object> port = new LinkedHashMap<>();
                    port.put("type", "OS::Neutron::Port");
                    port.put("properties", Map.of(
                            "fixed_ips", List.of(Map.of(
                                    "subnet", Map.of("get_resource", StrUtil.format("subnet_{}", ipInfo.getNodeId())),
                                    "ip_address", ipInfo.getIp()
                            )),
                            "name", StrUtil.format("port_{}_{}", node.getId(), i),
                            "network", Map.of("get_resource", StrUtil.format("net_{}", ipInfo.getNodeId())),
                            "port_security_enabled", false)
                    );
                    resourcesData.put(StrUtil.format("port_{}_{}", node.getId(), i), port);

                    // 创建路由器接口
                    Map<String, Object> interface_ = new LinkedHashMap<>();
                    interface_.put("type", "OS::Neutron::RouterInterface");
                    interface_.put("properties", Map.of(
                            "router_id", Map.of("get_resource", StrUtil.format("router_{}", node.getId())),
                            "port", Map.of("get_resource", StrUtil.format("port_{}_{}", node.getId(), i))
                    ));
                    resourcesData.put(StrUtil.format("router_interface_{}_{}", node.getId(), i), interface_);
                }

                // 添加静态路由
                if (node.getRouterInfo().getStaticRoutingInfos() != null && !node.getRouterInfo().getStaticRoutingInfos().isEmpty()) {
                    List<Map<String, Object>> routes = new ArrayList<>();
                    for (int i = 0; i < node.getRouterInfo().getStaticRoutingInfos().size(); i++) {
                        Node.RouterInfo.StaticRoutingInfo staticRouteInfo = node.getRouterInfo().getStaticRoutingInfos().get(i);
                        routes.add(Map.of("destination", staticRouteInfo.getDestinationCIDR(), "nexthop", staticRouteInfo.getNextHop()));
                    }
                    Map<String, Object> staticRoute = new LinkedHashMap<>();
                    staticRoute.put("type", "OS::Neutron::ExtraRouteSet");
                    staticRoute.put("properties", Map.of(
                            "router", Map.of("get_resource", StrUtil.format("router_{}", node.getId())),
                            "routes", routes
                    ));
                    resourcesData.put(StrUtil.format("extra_route_set_{}", node.getId()), staticRoute);
                }
            }
        }

        // 解析instance
        for (Node node : nodes) {
            if (Objects.equals(node.getType(), NodeTypeEnum.INSTANCE.getCode())) {

                List<Map<String, Object>> ports = new ArrayList<>();

                // 创建交换机端口
                for (int i = 0; i < node.getInstanceInfo().getIpInfos().size(); i++) {
                    Node.IpInfo ipInfo = node.getInstanceInfo().getIpInfos().get(i);
                    // 创建交换机端口
                    Map<String, Object> port = new LinkedHashMap<>();
                    port.put("type", "OS::Neutron::Port");
                    port.put("properties", Map.of(
                            "fixed_ips", List.of(Map.of(
                                    "subnet", Map.of("get_resource", StrUtil.format("subnet_{}", ipInfo.getNodeId())),
                                    "ip_address", ipInfo.getIp()
                            )),
                            "name", StrUtil.format("port_{}_{}", node.getId(), i),
                            "network", Map.of("get_resource", StrUtil.format("net_{}", ipInfo.getNodeId())),
                            "port_security_enabled", false)
                    );
                    resourcesData.put(StrUtil.format("port_{}_{}", node.getId(), i), port);
                    ports.add(Map.of("port", Map.of("get_resource", StrUtil.format("port_{}_{}", node.getId(), i))));
                }
                // 创建实例
                Map<String, Object> instance = new LinkedHashMap<>();
                instance.put("type", "OS::Nova::Server");
                instance.put("properties", Map.of(
                        "image", node.getInstanceInfo().getImage(),
                        "flavor", node.getInstanceInfo().getFlavor(),
                        "name", node.getId(),
                        "networks", ports));
                resourcesData.put(StrUtil.format("server_{}", node.getId()), instance);
            }
        }
        // 生成heat模板
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(dumperOptions);
        String output = yaml.dump(basicData);
        return output;
    }

    /**
     * 创建stack
     */
    public boolean createStack(String template) {
        return true;
    }
}
