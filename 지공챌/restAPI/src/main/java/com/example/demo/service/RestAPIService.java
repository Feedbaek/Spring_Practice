package com.example.demo.service;

import com.example.demo.dto.Text;
import com.example.demo.repository.MyRepository;
import org.springframework.stereotype.Service;

@Service
public class RestAPIService {

    private final MyRepository myRepository;

    public RestAPIService(MyRepository myRepository) {
        this.myRepository = myRepository;
    }

    public Text getText(String title) {
        return (Text) myRepository.findByKey(title);
    }

    public void saveText(Text text) {
        myRepository.save(text.getTitle(), text);
    }
}
