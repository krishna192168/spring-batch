package com.dreambuilders.springbatch.controller;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class COVIDController {
    @Autowired
    Job job;

    @Autowired
    JobLauncher jobLauncher;

    @GetMapping("/load")
    public String loadColossalData() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        Map<String, JobParameter> maps = new HashMap<>();
        maps.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters parameters = new JobParameters(maps);
        JobExecution jobExecution = jobLauncher.run(job, parameters);

//        System.out.println("JobExecution: " + jobExecution.getStatus());

        System.out.println("Batch is Running...");
//        while (jobExecution.isRunning()) {
//            System.out.println("...");
//        }

//        return jobExecution.getStatus();
        return "Spring batch colossal data process";
    }
}
