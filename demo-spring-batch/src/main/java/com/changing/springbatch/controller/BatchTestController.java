package com.changing.springbatch.controller;

import java.util.Date;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenjun
 * @version V1.0
 * @since 2020-11-13 11:07
 */
@RestController
@RequestMapping("/batch/test")
public class BatchTestController {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private ApplicationContext applicationContext;

    @RequestMapping("/execute")
    public void executeJob(@RequestParam Map<String, String> param) {
        String jobName = param.get("jobName");

        Job helloWorldJob = applicationContext.getBean(jobName, Job.class);

        try {
            JobParameters jobParameters = new JobParametersBuilder().addDate("date", new Date()).toJobParameters();
            jobLauncher.run(helloWorldJob, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }

}