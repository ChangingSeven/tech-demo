package com.changing.springbatch.config.demo;

import com.changing.springbatch.model.Person;
import com.changing.springbatch.processor.PersonItemProcessor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

/**
 * 多线程读写
 *
 * @author chenjun
 * @version V1.0
 * @since 2020-10-29 17:44
 */
@Configuration
public class Job6Config {

    @Bean
    public Job job6(JobBuilderFactory jobBuilders, StepBuilderFactory stepBuilders) {

        return jobBuilders.get("job6Name").start(job6Step1(stepBuilders)).build();
    }

    @Bean
    public Step job6Step1(StepBuilderFactory stepBuilders) {
        return stepBuilders.get("job6Step1Name").<Person, String>chunk(10).reader(job6Reader())
            .processor(new PersonItemProcessor()).writer(job6Writer()).taskExecutor(job6TaskExecutor())
            // 线程并发数
            .throttleLimit(10).build();
    }

    private FlatFileItemReader<Person> job6Reader() {
        return new FlatFileItemReaderBuilder<Person>().name("personItemReader")
            .resource(new ClassPathResource("sourcefile/persons.csv")).delimited()
            .names(new String[] { "firstName", "lastName" }).targetType(Person.class).build();
    }

    private FlatFileItemWriter<String> job6Writer() {
        return new FlatFileItemWriterBuilder<String>().name("greetingItemWriter")
            .resource(new FileSystemResource("target/test-outputs/job6_greetings.txt"))
            .lineAggregator(new PassThroughLineAggregator<>()).build();
    }

    private TaskExecutor job6TaskExecutor() {
        // 异步多线程
        return new SimpleAsyncTaskExecutor("job6_task_executor");
    }

}