package com.example.test.http;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rest")
public class RestClientController {

    private final RestClientService restClientService;

    // 동기식 호출 및 응답
    @GetMapping("/caller")
    public String caller() {
        return restClientService.get();
    }

    // 동기식 응답
    @GetMapping("/callee")
    public String callee() {
        return "Hello from rest callee";
    }
}
