package com.changing.springbatch.config.demo;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenjun
 * @version V1.0
 * @since 2020-11-30 15:52
 */
@Configuration
public class Tasklet1Config {

    @Bean
    public Job tasklet1(JobBuilderFactory jobBuilders, StepBuilderFactory stepBuilders) {
        return jobBuilders.get("tasklet1Name").start(tasklet1Step1(stepBuilders)).next(tasklet1Step2(stepBuilders))
            .build();
    }

    private Step tasklet1Step1(StepBuilderFactory stepBuilders) {
        return stepBuilders.get("tasklet1Step1Name").tasklet((stepContribution, chunkContext) -> {
            System.out.println("tasklet1Step1 Tasklet.execute 方法执行...");

            return RepeatStatus.FINISHED;
        }).build();
    }

    private Step tasklet1Step2(StepBuilderFactory stepBuilders) {
        return stepBuilders.get("tasklet1Step2Name").tasklet((stepContribution, chunkContext) -> {
            System.out.println("tasklet1Step2 Tasklet.execute 方法执行...");

            return RepeatStatus.FINISHED;
        }).build();
    }

}