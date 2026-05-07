package com.example.demo.image.controller;

import com.example.demo.identity.service.IdentityService;
import com.example.demo.image.response.Image;
import com.example.demo.image.response.ImageListResponse;
import com.example.demo.r.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private IdentityService identityService;

    @GetMapping("/list")
    public R<ImageListResponse> list() {
        var os = identityService.getOSClientV3();
        var images = os.imagesV2().list();

        List<Image> result = new ArrayList<>();
        for (var image : images) {
            Image temp = new Image();
            temp.setId(image.getId());
            temp.setName(image.getName());
            temp.setStatus(image.getStatus() != null ? image.getStatus().value() : null);
            temp.setDiskFormat(image.getDiskFormat() != null ? image.getDiskFormat().value() : null);
            temp.setSize(image.getSize());
            temp.setChecksum(image.getChecksum());
            temp.setCreatedAt(image.getCreatedAt() != null ? image.getCreatedAt().toString() : null);
            temp.setUpdatedAt(image.getUpdatedAt() != null ? image.getUpdatedAt().toString() : null);
            result.add(temp);
        }
        ImageListResponse imageListResponse = new ImageListResponse();
        imageListResponse.setImages(result);

        return R.ok(imageListResponse);
    }
}