package com.example.demo.orchestration.controller;


import com.example.demo.identity.service.IdentityService;
import com.example.demo.orchestration.request.CreateStackRequest;
import com.example.demo.orchestration.request.GetStackDetailsRequest;
import com.example.demo.orchestration.response.CreateStackResponse;
import com.example.demo.orchestration.response.GetStackDetailsResponse;
import com.example.demo.orchestration.service.OrchestrationService;
import com.example.demo.r.R;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.VNCConsole;
import org.openstack4j.model.heat.Resource;
import org.openstack4j.model.heat.Stack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/orchestration")
public class OrchestrationController {
    @Autowired
    private OrchestrationService orchestrationService;
    @Autowired
    private IdentityService identityService;

    @PostMapping("/createStack")
    public R<CreateStackResponse> createStack(@RequestBody CreateStackRequest createStackRequest) {
        String template = orchestrationService.createTemplate(createStackRequest.getNodes());
        CreateStackResponse response = orchestrationService.createStack(template);
        return R.ok(response);
    }

    @PostMapping("/getStackDetails")
    public R<GetStackDetailsResponse> getStackDetails(@RequestBody GetStackDetailsRequest request) {
        OSClient.OSClientV3 os = identityService.getOSClientV3();
        Stack stack = os.heat().stacks().getDetails(request.getStackName(), request.getStackId());

        Map<String, String> nodeIdToVNCConsoleUrl = new HashMap<>();
        if (Objects.equals("CREATE_COMPLETE", stack.getStatus())) {
            List<? extends Resource> resources = os.heat().resources().list(request.getStackName(), request.getStackId());
            for (Resource resource : resources) {
                if (Objects.equals("OS::Nova::Server", resource.getType())) {
                    String nodeId = resource.getResourceName().replace("server_", "");
                    VNCConsole console = os.compute().servers().getVNCConsole(resource.getPhysicalResourceId(), VNCConsole.Type.NOVNC);
                    nodeIdToVNCConsoleUrl.put(nodeId, console.getURL());
                }
            }
        }

        GetStackDetailsResponse response = new GetStackDetailsResponse();
        response.setStatus(stack.getStatus());
        response.setNodeIdToVNCConsoleUrl(nodeIdToVNCConsoleUrl);
        return R.ok(response);
    }
}
