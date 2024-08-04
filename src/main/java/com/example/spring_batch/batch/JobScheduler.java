package com.example.spring_batch.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Log4j2(topic = "job-scheduler")
@RequiredArgsConstructor
public class JobScheduler {
    private final JobLauncher jobLauncher;
    private final Job exampleJob1;
    private final Job exampleJob2;

    @Scheduled(fixedDelay = 5000)  // 5초마다 실행
    public void runJob1() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        Map<String, JobParameter<?>> jobParametersMap = new HashMap<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        String date = simpleDateFormat.format(new Date());
        JobParameter<String> jobParameter = new JobParameter<>(date, String.class);

        jobParametersMap.put("date", jobParameter);
        JobParameters jobParameters = new JobParameters(jobParametersMap);

        JobExecution jobExecution1 = jobLauncher.run(exampleJob1, jobParameters);
        JobExecution jobExecution2 = jobLauncher.run(exampleJob2, jobParameters);

        while (jobExecution1.isRunning() || jobExecution2.isRunning()) {
            log.info("Job is running...");
        }

        log.info("Job1 Execution: {}", jobExecution1.getStatus());
        log.info("Job1 getJobId: {}", jobExecution1.getJobId());
        log.info("Job1 getExitStatus: {}", jobExecution1.getExitStatus());
        log.info("Job1 getJobInstance: {}", jobExecution1.getJobInstance());
        log.info("Job1 getStepExecutions: {}", jobExecution1.getStepExecutions());
        log.info("Job1 getLastUpdated: {}", jobExecution1.getLastUpdated());
        log.info("Job1 getFailureExceptions: {}", jobExecution1.getFailureExceptions());

        log.info("Job2 Execution: {}", jobExecution2.getStatus());
        log.info("Job2 getJobId: {}", jobExecution2.getJobId());
        log.info("Job2 getExitStatus: {}", jobExecution2.getExitStatus());
        log.info("Job2 getJobInstance: {}", jobExecution2.getJobInstance());
        log.info("Job2 getStepExecutions: {}", jobExecution2.getStepExecutions());
        log.info("Job2 getLastUpdated: {}", jobExecution2.getLastUpdated());
        log.info("Job2 getFailureExceptions: {}", jobExecution2.getFailureExceptions());
    }
}
