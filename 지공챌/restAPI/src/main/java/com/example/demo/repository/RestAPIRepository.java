package com.example.demo.repository;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class RestAPIRepository {

    private final Map<String, Object> data = new HashMap<>();

    public Object findByKey(String key) {
        return data.get(key);
    }

    public void save(String key, Object value) {
        data.put(key, value);
    }
}
