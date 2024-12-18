package com.example.test.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.Map;

@DisplayName("필터 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CustomFilterTest {

    @Autowired
    private RestClient restClient;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("헤더 정보 가져오기")
    public void getSomething() throws JsonProcessingException {
        // given
        String url = "http://localhost:8080/custom/header";

        // when
        var result = restClient.get()
                .uri(url)
                .retrieve()
                .body(Map.class);

        // then
        String prettyResult = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
        System.out.println(prettyResult);
    }
}
