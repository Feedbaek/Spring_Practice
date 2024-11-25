package com.example.spring_batch.db.entity;

import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@Entity
@Table(name = "emp")
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Emp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String dept;
    private String salary;

    public static Emp of(Long id, String name, String dept, String salary) {
        return Emp.builder()
                .id(id)
                .name(name)
                .dept(dept)
                .salary(salary)
                .build();
    }
}
