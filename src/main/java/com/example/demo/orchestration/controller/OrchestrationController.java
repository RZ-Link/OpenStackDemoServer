package com.example.demo.orchestration.controller;


import com.example.demo.orchestration.request.CreateStackRequest;
import com.example.demo.orchestration.service.OrchestrationService;
import com.example.demo.r.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orchestration")
public class OrchestrationController {
    @Autowired
    private OrchestrationService orchestrationService;

    @PostMapping("/createStack")
    public R<Boolean> createStack(@RequestBody CreateStackRequest createStackRequest) {
        String template = orchestrationService.createTemplate(createStackRequest.getNodes());
        boolean result = orchestrationService.createStack(template);
        return R.ok(result);
    }
}
