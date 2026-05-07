package com.example.demo.compute.response;

import lombok.Data;

/**
 * 实例类型
 */
@Data
public class Flavor {
    // ID
    private String id;
    // 名称
    private String name;
    // vcpu数量
    private Integer vcpus;
    // 内存（MB）
    private Integer ram;
    // 硬盘（GB）
    private Integer disk;
}
