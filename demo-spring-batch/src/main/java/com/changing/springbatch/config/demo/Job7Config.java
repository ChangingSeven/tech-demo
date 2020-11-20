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
import org.springframework.core.task.TaskExecutor;

/**
 * 步骤并行执行
 *
 * @author chenjun
 * @version V1.0
 * @since 2020-10-29 17:44
 */
@Configuration
public class Job7Config {

    @Bean
    public Job job7(JobBuilderFactory jobBuilders, StepBuilderFactory stepBuilders) {

        return jobBuilders.get("job7Name").start(job7SplitFlow(stepBuilders)).next(job7Step4(stepBuilders))
            .build()        //builds FlowJobBuilder instance
            .build();
    }

    @Bean
    public Flow job7SplitFlow(StepBuilderFactory stepBuilders) {
        return new FlowBuilder<SimpleFlow>("splitFlow").split(job7TaskExecutor())
            .add(job7Flow1(stepBuilders), job7Flow2(stepBuilders)).build();
    }

    @Bean
    public Flow job7Flow1(StepBuilderFactory stepBuilders) {
        return new FlowBuilder<SimpleFlow>("job7Flow1").start(job7Step1(stepBuilders)).next(job7Step2(stepBuilders))
            .build();
    }

    @Bean
    public Flow job7Flow2(StepBuilderFactory stepBuilders) {
        return new FlowBuilder<SimpleFlow>("job7Flow2").start(job7Step3(stepBuilders)).build();
    }

    @Bean
    public Step job7Step1(StepBuilderFactory stepBuilders) {
        return stepBuilders.get("job7Step1Name").<Person, Person>chunk(10).reader(job7ItemReader())
            .writer(job7ItemWriter()).build();
    }

    @Bean
    public Step job7Step2(StepBuilderFactory stepBuilders) {
        return stepBuilders.get("job7Step2Name").<Person, Person>chunk(10).reader(job7ItemReader())
            .writer(job7ItemWriter()).build();
    }

    @Bean
    public Step job7Step3(StepBuilderFactory stepBuilders) {
        return stepBuilders.get("job7Step3Name").<Person, Person>chunk(10).reader(job7ItemReader())
            .writer(job7ItemWriter()).build();
    }

    @Bean
    public Step job7Step4(StepBuilderFactory stepBuilders) {
        return stepBuilders.get("job7Step4Name").<Person, Person>chunk(10).reader(job7ItemReader())
            .writer(job7ItemWriter()).build();
    }

    private ItemReader<Person> job7ItemReader() {
        return new FlatFileItemReaderBuilder<Person>().name("job7ItemReader")
            .resource(new ClassPathResource("sourcefile/persons.csv")).delimited()
            .names(new String[] { "firstName", "lastName" }).targetType(Person.class).build();
    }

    private ItemWriter<Person> job7ItemWriter() {
        DelimitedLineAggregator<Person> delimitedLineAggregator = new DelimitedLineAggregator<>();
        BeanWrapperFieldExtractor<Person> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<>();
        beanWrapperFieldExtractor.setNames(new String[] { "firstName", "lastName" });
        delimitedLineAggregator.setDelimiter(",");
        delimitedLineAggregator.setFieldExtractor(beanWrapperFieldExtractor);

        return new FlatFileItemWriterBuilder<Person>().name("job7ItemWriter")
            .resource(new FileSystemResource("target/test-outputs/job7_output.csv"))
            .lineAggregator(delimitedLineAggregator).build();
    }

    private TaskExecutor job7TaskExecutor() {
        // 异步多线程
        return new SimpleAsyncTaskExecutor("job7_task_executor");
    }

}