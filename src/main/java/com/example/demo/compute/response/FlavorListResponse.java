package com.example.demo.compute.response;

import lombok.Data;

import java.util.List;

@Data
public class FlavorListResponse {
    private List<Flavor> flavors;
}
