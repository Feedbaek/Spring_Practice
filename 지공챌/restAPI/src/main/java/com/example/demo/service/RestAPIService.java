package com.example.demo.service;

import com.example.demo.dto.Text;
import com.example.demo.repository.RestAPIRepository;
import org.springframework.stereotype.Service;

@Service
public class RestAPIService {

    private final RestAPIRepository restAPIRepository;

    public RestAPIService(RestAPIRepository restAPIRepository) {
        this.restAPIRepository = restAPIRepository;
    }

    public Text getText(String title) {
        return (Text) restAPIRepository.findByKey(title);
    }

    public void saveText(Text text) {
        restAPIRepository.save(text.getTitle(), text);
    }
}
