package com.universite.apirest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "SCL.lombok BACKEND";
    }
}