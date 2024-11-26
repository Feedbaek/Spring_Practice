package com.example.spring_batch.batch_ex3;

import com.example.spring_batch.db.dto.EmpDto;
import com.example.spring_batch.db.entity.Emp;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class Example3Job1Config {

    private final PlatformTransactionManager transactionManager;
    private final JobRepository jobRepository;

    private final FlatFileItemReader<EmpDto> reader;
    private final ItemProcessor<EmpDto, Emp> processor;
    private final RepositoryItemWriter<Emp> writer;

    /**
     * csv 파일을 읽어서 JPA를 사용해 DB에 저장하는 Job
     * */
    @Bean
    public Job example3Job1() {
        return new JobBuilder("example3Job1", jobRepository)
                .start(example3Step1())
                .build();
    }

    @Bean
    public Step example3Step1() {
        return new StepBuilder("example3Step1", jobRepository)
                .<EmpDto, Emp>chunk(1, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
