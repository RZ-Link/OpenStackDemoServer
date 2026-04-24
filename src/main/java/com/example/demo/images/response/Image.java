package com.example.demo.images.response;

import lombok.Data;

@Data
public class Image {
    // ID
    private String id;
    // 名称
    private String name;
    // 状态（https://docs.openstack.org/glance/latest/user/statuses.html）
    private String status;
    // 磁盘格式（https://docs.openstack.org/glance/latest/user/formats.html）
    private String diskFormat;
    // 镜像文件实际大小（字节）
    private Long size;
    // 镜像MD5校验和
    private String checksum;
    // 镜像创建时间
    private String createdAt;
    // 镜像更新时间
    private String updatedAt;

}
