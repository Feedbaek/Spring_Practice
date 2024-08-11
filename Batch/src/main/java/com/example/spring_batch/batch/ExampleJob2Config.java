package com.example.spring_batch.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Log4j2(topic = "example-job2-config")
@Configuration
public class ExampleJob2Config {
    /**
     *  Flow를 가진 Job 생성
     * */
    @Bean
    public Job exampleJob2(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new JobBuilder("exampleJob2", jobRepository)
                .start(oneStep(jobRepository, platformTransactionManager))
                        .on("FAILED")  // oneStep의 ExitStatus가 FAILED일 경우
                        .to(failureStep(jobRepository, platformTransactionManager))  // failureStep 실행
                        .on("*")  // failureStep의 결과에 상관없이
                        .to(writeStep(jobRepository, platformTransactionManager))  // writeStep 실행
                        .on("*")  // writeStep의 결과에 상관없이
                        .end()  // Flow 종료

                .from(oneStep(jobRepository, platformTransactionManager))  // oneStep의 ExitStatus가 FAILED가 아니고
                        .on("COMPLETED")  //  COMPLETED일 경우
                        .to(successStop(jobRepository, platformTransactionManager))  // successStop 실행
                        .on("*")  // successStop의 결과에 상관없이
                        .to(writeStep(jobRepository, platformTransactionManager))  // writeStep 실행
                        .on("*")  // writeStep의 결과에 상관없이
                        .end()  // Flow 종료

                .from(oneStep(jobRepository, platformTransactionManager))  // successStop의 결과에
                        .on("*")  // 상관없이
                        .to(writeStep(jobRepository, platformTransactionManager))  // writeStep 실행
                        .on("*")  // writeStep의 결과에 상관없이
                        .end()  // Flow 종료
                .end()
                .build();
    }

    @Bean
    public Step oneStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("oneStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    log.info("This is a first job.");
                    // Flow에서 on은 RepeatStatus가 아닌 ExitStatus를 기준으로 함
                    contribution.setExitStatus(ExitStatus.COMPLETED);

                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step failureStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("failureStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    log.info("This is a failure job.");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step writeStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("writeStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    log.info("This is a write job.");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step successStop(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("successStop", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    log.info("This is a success job.");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
}