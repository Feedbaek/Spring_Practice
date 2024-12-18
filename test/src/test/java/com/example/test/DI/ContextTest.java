package com.example.test.DI;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@SpringBootTest
@RequiredArgsConstructor
public class ContextTest {

    @Autowired
    private A a;

    @Autowired
    private D d;

    @TestConfiguration
    static class ContextConfiguration {

        @Bean
        public A b() {
            return new B();
        }

        @Bean
        @Primary
        public C c() {
            return new C();
        }

        @Bean
        public D d() {
            return new D(b());
        }
    }

    @Test
    @DisplayName("DI 테스트")
    public void doSomething() {
        a.doSomething();

        d.doSomething();
    }
}
