package com.example.gRPC_client.controller;

import com.example.gRPC_client.grpc.GrpcClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HelloController {

    private final GrpcClientService grpcClientService;

    @GetMapping("/hello")
    public String sayHello(@RequestParam String name) {
        // gRPC 서버에 "World"라는 이름으로 인사 요청
        return grpcClientService.sayHello(name);
    }
}
