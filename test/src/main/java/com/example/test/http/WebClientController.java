package com.example.test.http;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/web")
public class WebClientController {

    private final WebClientService webClientService;

    // 비동기식 호출 및 응답
    @GetMapping("/caller")
    public Mono<String> caller() {
        return webClientService.get();
    }

    // 지연된 동기식 응답
    @SneakyThrows
    @GetMapping("/callee")
    public String callee() {
        Thread.sleep(2000);
        System.out.println("callee called");
        return "Hello from web callee";
    }
}