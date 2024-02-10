package com.home.mower.runner;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class JobRunner implements CommandLineRunner {
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("mowerBatchJob")
    private Job mowerBatchJob;

    @Override
    public void run(String... args) throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .toJobParameters();
        jobLauncher.run(mowerBatchJob, jobParameters);
    }
}
