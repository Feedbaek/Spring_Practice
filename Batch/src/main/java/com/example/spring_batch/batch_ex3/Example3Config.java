package com.example.spring_batch.batch_ex3;

import com.example.spring_batch.db.dto.EmpDto;
import com.example.spring_batch.db.entity.Emp;
import com.example.spring_batch.db.repository.EmpRepository;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class Example3Config {

    private final EmpRepository empRepository;
    private final SqlSessionFactory sqlSessionFactory;

    @Bean
    public FlatFileItemReader<EmpDto> fileReader() {
        return new FlatFileItemReaderBuilder<EmpDto>()
                .name("empReader")
                .resource(new ClassPathResource("emp/data.csv"))
                .delimited()
                .names("id", "name", "dept", "salary")
                .targetType(EmpDto.class)
                .build();
    }

    @Bean
    @StepScope
    public ListItemReader<Emp> failedItemsReader(@Value("#{stepExecution}") StepExecution stepExecution) {
        ExecutionContext executionContext = stepExecution.getExecutionContext();
        List<Emp> failedItems = (List<Emp>) executionContext.get("failedItems");
        System.out.println("failedItems: " + failedItems);
        return new ListItemReader<>(failedItems == null ? Collections.emptyList() : failedItems);
    }

    @Bean
    public ItemProcessor<EmpDto, Emp> processor() {
        return item -> Emp.of(Long.valueOf(item.getId()), item.getName(), item.getDept(), item.getSalary());
    }

    @Bean
    public RepositoryItemWriter<Emp> jpaWriter() {
        RepositoryItemWriter<Emp> writer = new RepositoryItemWriter<>();
        writer.setRepository(empRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public MyBatisBatchItemWriter<Emp> mybatisInsertWriter() {
        MyBatisBatchItemWriter<Emp> writer = new MyBatisBatchItemWriter<>();
        writer.setSqlSessionFactory(sqlSessionFactory);
        writer.setStatementId("com.example.spring_batch.db.mapper.EmpMapper.insertEmp");
        return writer;
    }

    @Bean
    @StepScope
    public MyBatisBatchItemWriter<Emp> mybatisUpdateWriter() {
        MyBatisBatchItemWriter<Emp> writer = new MyBatisBatchItemWriter<>();
        writer.setSqlSessionFactory(sqlSessionFactory);
        writer.setStatementId("com.example.spring_batch.db.mapper.EmpMapper.updateEmp");
        return writer;
    }

    @Bean
    @StepScope
    public ItemWriteListener<Emp> MyItemWriteListener() {
        return new ItemWriteListener<>() {
            @Override
            public void onWriteError(Exception ex, Chunk<? extends Emp> item) {
                StepExecution stepExecution = StepSynchronizationManager.getContext().getStepExecution();
                ExecutionContext executionContext = stepExecution.getExecutionContext();

                List<Emp> failedItems = (List<Emp>) executionContext.get("failedItems");
                if (failedItems == null) {
                    failedItems = new ArrayList<>();
                    executionContext.put("failedItems", failedItems);
                }
                failedItems.addAll(item.getItems());
            }
        };
    }
}
