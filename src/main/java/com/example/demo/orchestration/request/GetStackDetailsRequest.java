package com.example.demo.orchestration.request;

import lombok.Data;

@Data
public class GetStackDetailsRequest {
    private String stackId;
    private String stackName;
}
