package com.changing.springbatch.config.demo;

import com.changing.springbatch.model.Person;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
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
import org.springframework.core.task.SimpleAsyncTaskExecutor;

/**
 * 并行执行
 * 执行顺序：job5Step1、job5Step2 并行执行 --> job5Step3
 *
 * @author chenjun
 * @version V1.0
 * @since 2020-10-29 17:44
 */
@Configuration
public class Job5Config {

    @Bean
    public Job job5(JobBuilderFactory jobBuilders, StepBuilderFactory stepBuilders) {
        // 分流执行step，相当于并行执行
        Flow flow1 = new FlowBuilder<SimpleFlow>("job5Flow1").start(job5Step1(stepBuilders)).build();

        return jobBuilders.get("job5Name").start(flow1).split(new SimpleAsyncTaskExecutor()).add(flow2(stepBuilders))
            .next(job5Step3(stepBuilders)).end().build();
    }

    @Bean
    public Flow flow2(StepBuilderFactory stepBuilders) {

        return new FlowBuilder<SimpleFlow>("job5Flow2").start(job5Step2(stepBuilders)).build();
    }

    @Bean
    public Step job5Step1(StepBuilderFactory stepBuilders) {
        return stepBuilders.get("job5Step1Name").<Person, Person>chunk(10).reader(job5ItemReader())
            .writer(job5ItemWriter()).build();
    }

    @Bean
    public Step job5Step2(StepBuilderFactory stepBuilders) {
        return stepBuilders.get("job5Step2Name").<Person, Person>chunk(10).reader(job5ItemReader())
            .writer(job5ItemWriter2()).build();
    }

    @Bean
    public Step job5Step3(StepBuilderFactory stepBuilders) {
        return stepBuilders.get("job5Step3Name").<Person, Person>chunk(10).reader(job5ItemReader())
            .writer(job5ItemWriter()).build();
    }

    private ItemReader<Person> job5ItemReader() {
        return new FlatFileItemReaderBuilder<Person>().name("job5ItemReader")
            .resource(new ClassPathResource("sourcefile/persons.csv")).delimited()
            .names(new String[] { "firstName", "lastName" }).targetType(Person.class).build();
    }

    private ItemWriter<Person> job5ItemWriter() {
        DelimitedLineAggregator<Person> delimitedLineAggregator = new DelimitedLineAggregator<>();
        BeanWrapperFieldExtractor<Person> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<>();
        beanWrapperFieldExtractor.setNames(new String[] { "firstName", "lastName" });
        delimitedLineAggregator.setDelimiter(",");
        delimitedLineAggregator.setFieldExtractor(beanWrapperFieldExtractor);

        return new FlatFileItemWriterBuilder<Person>().name("job5ItemWriter")
            .resource(new FileSystemResource("target/test-outputs/job5_output.csv"))
            .lineAggregator(delimitedLineAggregator).build();
    }

    private ItemWriter<Person> job5ItemWriter2() {
        DelimitedLineAggregator<Person> delimitedLineAggregator = new DelimitedLineAggregator<>();
        BeanWrapperFieldExtractor<Person> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<>();
        beanWrapperFieldExtractor.setNames(new String[] { "firstName", "lastName" });
        delimitedLineAggregator.setDelimiter(",");
        delimitedLineAggregator.setFieldExtractor(beanWrapperFieldExtractor);

        return new FlatFileItemWriterBuilder<Person>().name("job5ItemWriter")
            .resource(new FileSystemResource("target/test-outputs/job5_output2.csv"))
            .lineAggregator(delimitedLineAggregator).build();
    }
}