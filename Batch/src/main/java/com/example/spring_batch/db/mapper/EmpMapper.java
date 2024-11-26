package com.example.spring_batch.db.mapper;

import com.example.spring_batch.db.entity.Emp;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmpMapper {

    void insertEmp(Emp emp);

    void updateEmp(Emp emp);
}
