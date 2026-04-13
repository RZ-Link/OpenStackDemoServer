package com.example.demo.r;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RController {

    @GetMapping("/hello-R")
    public R<String> helloR() {
        return R.ok("Hello R!");
    }
}
