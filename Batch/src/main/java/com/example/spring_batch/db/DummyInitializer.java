package com.example.spring_batch.db;

import com.example.spring_batch.db.entity.Emp;
import com.example.spring_batch.db.repository.EmpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DummyInitializer implements CommandLineRunner {
    private final EmpRepository empRepository;

    private void empCreate() {
        for (int i = 0; i < 100; i++) {
            empRepository.save(Emp.builder()
                    .name("name" + i)
                    .dept("dept" + i)
                    .salary("salary" + i)
                    .build());
        }
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (empRepository.count() < 100) {
            empCreate();
        }
    }
}
