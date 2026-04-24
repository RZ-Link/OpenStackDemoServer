package com.example.demo.images.controller;

import com.example.demo.identity.service.IdentityService;
import com.example.demo.images.response.Image;
import com.example.demo.images.response.ImagesListResponse;
import com.example.demo.r.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/images")
public class ImagesController {

    @Autowired
    private IdentityService identityService;

    @GetMapping("/list")
    @ResponseBody
    public R<ImagesListResponse> list() {
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
        ImagesListResponse imagesListResponse = new ImagesListResponse();
        imagesListResponse.setImages(result);

        return R.ok(imagesListResponse);
    }
}