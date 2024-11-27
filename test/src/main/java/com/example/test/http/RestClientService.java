package com.example.test.http;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class RestClientService {

    private final RestClient restClient;

    public String get() {

        return restClient.get()
                .uri("http://localhost:8080/rest/callee")
                .retrieve()
                .body(String.class);
    }
}
