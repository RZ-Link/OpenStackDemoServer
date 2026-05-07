package com.example.demo.image.response;

import lombok.Data;

import java.util.List;

@Data
public class ImageListResponse {
    private List<Image> images;
}