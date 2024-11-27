package com.example.spring_batch.batch_ex1;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Log4j2(topic = "example1-job1-config")
@Configuration
public class Example1_1 {
    /**
     * 여러개의 Step과 Tasklet을 가진 Job 생성
     * */
    @Bean
    public Job example1Job1(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new JobBuilder("example1Job1", jobRepository)
                .start(oneStep(jobRepository, platformTransactionManager))
                .next(twoStep(jobRepository, platformTransactionManager))
                .next(threeStep(jobRepository, platformTransactionManager))
                .build();
    }

    private Step oneStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("oneStep", jobRepository)
                .tasklet(firstTasklet(), transactionManager)
                .tasklet(secondTasklet(), transactionManager)
                .tasklet(thirdTasklet(), transactionManager)
                .build();
    }

    private Step twoStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("twoStep", jobRepository)
                .tasklet(thirdTasklet(), transactionManager)
                .tasklet(firstTasklet(), transactionManager)
                .tasklet(secondTasklet(), transactionManager)
                .build();
    }

    private Step threeStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("threeStep", jobRepository)
                .tasklet(thirdTasklet(), transactionManager)
                .tasklet(secondTasklet(), transactionManager)
                .tasklet(firstTasklet(), transactionManager)
                .build();
    }

    private Tasklet firstTasklet() {
        return (contribution, chunkContext) -> {
            log.info("This is a first job.");
            return RepeatStatus.FINISHED;
        };
    }

    private Tasklet secondTasklet() {
        return (contribution, chunkContext) -> {
            log.info("This is a second job.");
            return RepeatStatus.FINISHED;
        };
    }

    private Tasklet thirdTasklet() {
        return (contribution, chunkContext) -> {
            log.info("This is a third job.");
            return RepeatStatus.FINISHED;
        };
    }
}
