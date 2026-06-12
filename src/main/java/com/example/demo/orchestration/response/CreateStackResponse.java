package com.example.demo.orchestration.response;

import lombok.Data;

@Data
public class CreateStackResponse {
    private String stackId;
    private String stackName;
}
