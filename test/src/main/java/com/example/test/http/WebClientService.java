package com.example.test.http;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WebClientService {

    private final WebClient webClient;

    public Mono<String> get() {

        Mono<String> res = webClient.get()
                .uri("http://localhost:8080/web/callee")
                .retrieve()
                .bodyToMono(String.class);

        System.out.println("res: " + res);

        return res;
    }
}
