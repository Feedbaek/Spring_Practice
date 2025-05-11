package com.example.demo.repository;

public interface MyRepository {

    Object findByKey(String key);

    void save(String key, Object value);
}
