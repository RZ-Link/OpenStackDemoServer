package com.example.demo.orchestration.enums;

import lombok.Getter;

@Getter
public enum NodeTypeEnum {

    SWITCH(1, "交换机"),
    ROUTER(2, "路由器"),
    INSTANCE(3, "实例");

    private Integer code;
    private String description;

    NodeTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}
