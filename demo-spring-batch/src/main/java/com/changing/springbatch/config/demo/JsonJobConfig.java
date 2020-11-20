package com.changing.springbatch.config.demo;

import com.changing.springbatch.model.Trade;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

/**
 * 读取json文件
 *
 * @author chenjun
 * @version V1.0
 * @since 2020-11-18 10:11
 */
@Configuration
public class JsonJobConfig {

    @Bean
    public Job jsonJob(JobBuilderFactory jobBuilders, StepBuilderFactory stepBuilders) {
        return jobBuilders.get("jsonJobName").start(jsonJobStep(stepBuilders)).build();
    }

    @Bean
    public Step jsonJobStep(StepBuilderFactory stepBuilders) {
        return stepBuilders.get("jsonJobStepName").<Trade, Trade>chunk(10).reader(jsonJobItemReader())
            .writer(jsonJobItemWriter()).build();
    }

    private JsonItemReader<Trade> jsonJobItemReader() {
        return new JsonItemReaderBuilder<Trade>().jsonObjectReader(new JacksonJsonObjectReader<>(Trade.class))
            .resource(new ClassPathResource("sourcefile/json_reader.json")).name("jsonJobItemReader").build();
    }

    private JsonFileItemWriter<Trade> jsonJobItemWriter() {
        return new JsonFileItemWriterBuilder<Trade>().jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
            .resource(new FileSystemResource("target/test-outputs/jsonjob_output.json")).name("jsonJobItemWriter")
            .build();
    }
}