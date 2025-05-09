package com.example.demo.controller;

import com.example.demo.dto.Text;
import com.example.demo.service.RestAPIService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RestAPIController {

    private final RestAPIService restAPIService;

    public RestAPIController(RestAPIService restAPIService) {
        this.restAPIService = restAPIService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    @GetMapping("/text")
    public Text getText(@RequestParam String title) {
        return restAPIService.getText(title);
    }

    @PostMapping("/text")
    public String saveText(@RequestBody Text text) {
        restAPIService.saveText(text);
        return "성공";
    }
}
