package com.changing.springbatch.config.demo;

import com.changing.springbatch.model.Person;

import java.io.FileNotFoundException;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
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
 * 条件执行，并通过实现自定义监听器，返回自定义的状态值
 *
 * @author chenjun
 * @version V1.0
 * @since 2020-10-29 17:44
 */
@Configuration
public class Job3Config {

    @Bean
    public Job job3(JobBuilderFactory jobBuilders, StepBuilderFactory stepBuilders) {
        // on 方法中的值是 ExitStatus 类中 exitCode 字段的值，可以集成 StepExecutionListenerSupport 返回自定义的 exitCode 值

        return jobBuilders.get("job3Name").start(job3Step1(stepBuilders)).on("FAILED").end()
            .from(job3Step1(stepBuilders)).on("COMPLETED WITH SKIPS").to(job3Step2(stepBuilders))
            .from(job3Step1(stepBuilders)).on("*").to(job3Step3(stepBuilders)).end().build();
    }

    @Bean
    public Step job3Step1(StepBuilderFactory stepBuilders) {
        return stepBuilders.get("job3Step1Name").<Person, Person>chunk(10).reader(job3ItemReader())
            .writer(job3ItemWriter()).faultTolerant().skip(FileNotFoundException.class).skipLimit(2)
            .listener(new SelfDefineStepExecutionListener()).build();
    }

    @Bean
    public Step job3Step2(StepBuilderFactory stepBuilders) {
        return stepBuilders.get("job3Step2Name").<Person, Person>chunk(10).reader(job3ItemReader())
            .writer(job3ItemWriter()).build();
    }

    @Bean
    public Step job3Step3(StepBuilderFactory stepBuilders) {
        return stepBuilders.get("job3Step3Name").<Person, Person>chunk(10).reader(job3ItemReader())
            .writer(job3ItemWriter()).build();
    }

    public class SelfDefineStepExecutionListener extends StepExecutionListenerSupport {

        @Override
        public ExitStatus afterStep(StepExecution stepExecution) {
            String exitCode = stepExecution.getExitStatus().getExitCode();
            if (!exitCode.equals(ExitStatus.FAILED.getExitCode()) && stepExecution.getSkipCount() > 0) {
                return new ExitStatus("COMPLETED WITH SKIPS");
            } else {
                return null;
            }
        }

    }

    private ItemReader<Person> job3ItemReader() {
        return new FlatFileItemReaderBuilder<Person>().name("job3ItemReader")
            .resource(new ClassPathResource("sourcefile/persons.csv")).delimited()
            .names(new String[] { "firstName", "lastName" }).targetType(Person.class).build();
    }

    private ItemWriter<Person> job3ItemWriter() {
        DelimitedLineAggregator<Person> delimitedLineAggregator = new DelimitedLineAggregator<>();
        BeanWrapperFieldExtractor<Person> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<>();
        beanWrapperFieldExtractor.setNames(new String[] { "firstName", "lastName" });
        delimitedLineAggregator.setDelimiter(",");
        delimitedLineAggregator.setFieldExtractor(beanWrapperFieldExtractor);

        return new FlatFileItemWriterBuilder<Person>().name("job3ItemWriter")
            .resource(new FileSystemResource("target/test-outputs/job3_output.csv"))
            .lineAggregator(delimitedLineAggregator).build();
    }
}