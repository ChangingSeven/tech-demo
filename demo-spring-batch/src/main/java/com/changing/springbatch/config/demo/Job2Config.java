package com.changing.springbatch.config.demo;

import com.changing.springbatch.model.Person;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

/**
 * 根据前置步骤执行情况，条件执行后续步骤
 * 执行顺序: job1Step1 --> job1Step2(job1Step1任意执行结果都会顺序执行)
 *                    --> job1Step3(job1Step1失败时才执行)
 *
 * @author chenjun
 * @version V1.0
 * @since 2020-10-29 17:44
 */
@Configuration
public class Job2Config {

    @Bean
    public Job job2(JobBuilderFactory jobBuilders, StepBuilderFactory stepBuilders) {
        return jobBuilders.get("job2Name").start(job2Step1(stepBuilders)).on("*").to(job2Step2(stepBuilders))
            .from(job2Step1(stepBuilders)).on("FAILED").to(job2Step3(stepBuilders)).end().build();
    }

    @Bean
    public Step job2Step1(StepBuilderFactory stepBuilders) {
        return stepBuilders.get("job2Step1Name").<Person, Person>chunk(10).reader(job2ItemReader())
            .writer(job2ItemWriter()).build();
    }

    @Bean
    public Step job2Step2(StepBuilderFactory stepBuilders) {
        return stepBuilders.get("job2Step2Name").<Person, Person>chunk(10).reader(job2ItemReader())
            .writer(job2ItemWriter()).build();
    }

    @Bean
    public Step job2Step3(StepBuilderFactory stepBuilders) {
        return stepBuilders.get("job2Step3Name").<Person, Person>chunk(10).reader(job2ItemReader())
            .writer(job2ItemWriter()).build();
    }

    private ItemReader<Person> job2ItemReader() {
        return new FlatFileItemReaderBuilder<Person>().name("job2ItemReader")
            .resource(new ClassPathResource("csv/persons.csv")).delimited()
            .names(new String[] { "firstName", "lastName" }).targetType(Person.class).build();
    }

    private ItemWriter<Person> job2ItemWriter() {
        DelimitedLineAggregator<Person> delimitedLineAggregator = new DelimitedLineAggregator<>();
        BeanWrapperFieldExtractor<Person> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<>();
        beanWrapperFieldExtractor.setNames(new String[] { "firstName", "lastName" });
        delimitedLineAggregator.setDelimiter(",");
        delimitedLineAggregator.setFieldExtractor(beanWrapperFieldExtractor);

        return new FlatFileItemWriterBuilder<Person>().name("job2ItemWriter")
            .resource(new FileSystemResource("target/test-outputs/job2_output.csv"))
            .lineAggregator(delimitedLineAggregator).build();
    }

}