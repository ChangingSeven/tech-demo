package com.changing.springbatch.config.demo;

import com.changing.springbatch.model.Person;
import com.changing.springbatch.processor.PersonItemProcessor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 * 读取多文件
 *
 * @author chenjun
 * @version V1.0
 * @since 2020-11-18 17:58
 */
@Configuration
public class MultiResourceReaderConfig {

    @Bean
    public Job multiResourceJob(JobBuilderFactory jobBuilders, StepBuilderFactory stepBuilders) {

        return jobBuilders.get("job6Name").start(multiResourceJobStep1(stepBuilders)).build();
    }

    private Step multiResourceJobStep1(StepBuilderFactory stepBuilders) {
        return stepBuilders.get("multiResourceJobStep1Name").<Person, String>chunk(10).reader(multiResourceReader())
            .processor(new PersonItemProcessor()).writer(singleResourceItemWriter()).build();
    }

    private MultiResourceItemReader multiResourceReader() {
        return new MultiResourceItemReaderBuilder<Person>().name("multiResourceReader").delegate(singleResourceReader())
            .resources(multiResource()).build();
    }

    private FlatFileItemWriter<String> singleResourceItemWriter() {
        return new FlatFileItemWriterBuilder<String>().name("singleResourceItemWriter")
            .resource(new FileSystemResource("target/test-outputs/multiResource_greetings.txt"))
            .lineAggregator(new PassThroughLineAggregator<>()).build();
    }

    private FlatFileItemReader<Person> singleResourceReader() {
        return new FlatFileItemReaderBuilder<Person>().name("singleResourceReader").delimited()
            .names(new String[] { "firstName", "lastName" }).targetType(Person.class).build();
    }

    private Resource[] multiResource() {

        List<Resource> list = new ArrayList<>();
        list.add(new ClassPathResource("sourcefile/persons.csv"));
        list.add(new ClassPathResource("sourcefile/persons02.csv"));

        return list.toArray(new Resource[list.size()]);
    }

}