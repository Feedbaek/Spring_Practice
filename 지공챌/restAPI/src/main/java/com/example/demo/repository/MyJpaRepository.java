package com.example.demo.repository;

import com.example.demo.dto.Text;
import com.example.demo.entity.MyEntity;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Primary
@Repository
public interface MyJpaRepository extends JpaRepository<MyEntity, String>, MyRepository {

    default void save(String title, Object value) {
        Text text = (Text) value;
        MyEntity entity = new MyEntity();
        entity.setKey(title);
        entity.setTitle(title);
        entity.setBody(text.getBody());
        this.save(entity);
    }

    default Object findByKey(String key) {
        MyEntity entity = this.findById(key).orElseThrow(
                () -> new RuntimeException("Entity not found")
        );

        Text text = new Text();
        text.setTitle(entity.getTitle());
        text.setBody(entity.getBody());

        return text;
    }
}
