package com.example.spring_batch.batch_ex3;

import com.example.spring_batch.db.dto.EmpDto;
import com.example.spring_batch.db.entity.Emp;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class Example3Job2Config {

    private final PlatformTransactionManager transactionManager;
    private final JobRepository jobRepository;

    private final FlatFileItemReader<EmpDto> reader;
    private final ListItemReader<Emp> failedItemsReader;

    private final ItemProcessor<EmpDto, Emp> processor;
    @Qualifier("mybatisInsertWriter")
    private final MyBatisBatchItemWriter<Emp> insertWriter;
    @Qualifier("mybatisUpdateWriter")
    private final MyBatisBatchItemWriter<Emp> updateWriter;
    private final ItemWriteListener<Emp> itemWriteListener;

    /**
     * csv 파일을 읽어서 Mybatis를 사용해 DB에 저장하는 Job
     * insert 실행 후 id 중복으로 실패하면 update 실행으로 예외 처리
     * */
    @Bean
    public Job example3Job2() {
        return new JobBuilder("example3Job2", jobRepository)
                .start(example3Step2())
                .next(example3ErrorStep())
                .build();
    }

    @Bean
    @JobScope
    public Step example3Step2() {
        return new StepBuilder("example3Step2", jobRepository)
                .<EmpDto, Emp>chunk(1, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(insertWriter)
                .faultTolerant()
                .skip(DuplicateKeyException.class)
                .skipLimit(100)
                .listener(itemWriteListener)
                .build();
    }

    @Bean
    @JobScope
    public Step example3ErrorStep() {
        return new StepBuilder("example3ErrorStep", jobRepository)
                .<Emp, Emp>chunk(1, transactionManager)
                .reader(failedItemsReader)
                .writer(updateWriter)
                .build();
    }
}
