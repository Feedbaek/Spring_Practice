package com.example.spring_batch.batch_ex3;

import com.example.spring_batch.db.dto.EmpDto;
import com.example.spring_batch.db.entity.Emp;
import com.example.spring_batch.db.repository.EmpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class Example3JobConfig {

    private final PlatformTransactionManager transactionManager;
    private final JobRepository jobRepository;

    private final EmpRepository empRepository;

    @Bean
    public Job example3Job() {
        return new JobBuilder("example3Job", jobRepository)
                .start(firstStep())
                .build();
    }

    @Bean
    public Step firstStep() {
        return new StepBuilder("firstStep", jobRepository)
                .<EmpDto, Emp>chunk(1, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public FlatFileItemReader<EmpDto> reader() {
        return new FlatFileItemReaderBuilder<EmpDto>()
                .name("empReader")
                .resource(new ClassPathResource("emp/data.csv"))
                .delimited()
                .names("id", "name", "dept", "salary")
                .targetType(EmpDto.class)
                .build();
    }

    @Bean
    public ItemProcessor<EmpDto, Emp> processor() {
        return item -> Emp.of(Long.valueOf(item.getId()), item.getName(), item.getDept(), item.getSalary());
    }

    @Bean
    public RepositoryItemWriter<Emp> writer() {
        RepositoryItemWriter<Emp> writer = new RepositoryItemWriter<>();
        writer.setRepository(empRepository);
        writer.setMethodName("save");
        return writer;
    }

}
