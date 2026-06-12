package com.example.demo.orchestration.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateStackRequest {
    private List<Node> nodes;
}
