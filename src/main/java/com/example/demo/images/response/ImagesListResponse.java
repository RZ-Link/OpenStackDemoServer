package com.example.demo.images.response;

import lombok.Data;

import java.util.List;

@Data
public class ImagesListResponse {
    private List<Image> images;
}