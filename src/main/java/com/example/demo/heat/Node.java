package com.example.demo.heat;

import lombok.Data;

import java.util.List;

/**
 * 节点
 * 包括交换机、路由器、实例（普通机器）
 */
@Data
public class Node {

    // 节点id
    private String id;
    // 节点名称
    private String name;
    // 节点类型，1交换机，2路由器，3实例（普通机器）
    private Integer type;

    // 如果节点类型是交换机，需要填写switchInfo，否则switchInfo是null
    private SwitchInfo switchInfo;

    // 如果节点类型是路由器，需要填写routerInfo，否则routerInfo是null
    private RouterInfo routerInfo;

    // 如果节点类型是实例（普通机器），需要填写instanceInfo，否则instanceInfo是null
    private InstanceInfo instanceInfo;

    // 交换机信息
    @Data
    public static class SwitchInfo {
        // 网络地址，需要符合cidr形式，比如10.0.0.0/24
        private String networkAddress;
        // 网关ip，比如10.0.0.254
        private String gatewayIp;
    }

    // 路由器信息
    @Data
    public static class RouterInfo {
        // 连接的交换机和ip
        private List<IpInfo> ipInfos;
        // 静态路由信息
        private List<StaticRoutingInfo> staticRouteInfos;

        @Data
        public static class StaticRoutingInfo {
            // 目的cidr，比如20.0.0.0/24
            private String destinationCIDR;
            // 下一跳，比如10.0.0.200
            private String nextHop;
        }
    }

    // 实例（普通机器）信息
    @Data
    public static class InstanceInfo {
        // 连接的交换机和ip
        private List<IpInfo> ipInfos;
        // 镜像id
        private String image;
        // 实例类型id
        private String flavor;
    }

    // 路由器、实例（普通机器）连接的交换机和ip
    @Data
    public static class IpInfo {
        // 连接的交换机
        private String nodeId;
        // ip
        private String ip;
    }

}
