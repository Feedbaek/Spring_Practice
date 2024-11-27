package com.example.spring_batch.batch_ex2;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 단순 성공 job, 단순 실패 job, 다중 분기 job, 중도 실패 job 만들어보기
 * */
@Log4j2(topic = "example2-job1-config")
@Configuration
@RequiredArgsConstructor
public class Example2_1 {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job example2Job1() {
        return new JobBuilder("example2Job1", jobRepository)
                .start(failStep())
                        .on("FAILED")
                        .to(successStep())
                        .on("*")
                        .end()
                .next(failStep())
                        .on("*")
                        .to(jobParameterStep(null))
                        .on("*")
                        .end()
                .end()
                .build();
    }

    private Step successStep() {
        return new StepBuilder("successStep", jobRepository)
                .tasklet((contribution, chunkContext) ->  {
                    log.info("+++++++ simpleTasklet ++++++++");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    private Step failStep() {
        return new StepBuilder("failStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    log.info("+++++++ failStep ++++++++");
                    contribution.setExitStatus(ExitStatus.FAILED);
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    @JobScope
    public Step jobParameterStep(@Value("#{jobParameters['requestDate']}") String requestDate) {
        return new StepBuilder("JobParameterStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    log.info("requestDate = {}", requestDate);
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    private Step chunkStep() {
        return new StepBuilder("chunkStep", jobRepository)
                .chunk(5, transactionManager)
                .build();
    }
}
