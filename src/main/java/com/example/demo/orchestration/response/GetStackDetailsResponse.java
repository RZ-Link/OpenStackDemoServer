package com.example.demo.orchestration.response;

import lombok.Data;

import java.util.Map;

@Data
public class GetStackDetailsResponse {
    private String status;

    private Map<String, String> nodeIdToVNCConsoleUrl;
}
