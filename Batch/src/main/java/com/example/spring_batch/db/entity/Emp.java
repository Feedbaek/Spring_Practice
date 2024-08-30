package com.example.spring_batch.db.entity;

import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@Entity
@Table(name = "emp")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Emp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String dept;
    private String salary;

    @Builder
    public Emp(String name, String dept, String salary) {
        this.name = name;
        this.dept = dept;
        this.salary = salary;
    }
}
