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
 * 条件执行，当步骤状态为指定状态时，终止批处理作业
 * 执行顺序：job4Step1 --> job4Step2 --> 失败 --> 终止作业
 *                                  --> 非失败 --> job4Step3
 * @author chenjun
 * @version V1.0
 * @since 2020-10-29 17:44
 */
@Configuration
public class Job4Config {

    @Bean
    public Job job4(JobBuilderFactory jobBuilders, StepBuilderFactory stepBuilders) {

        return jobBuilders.get("job4Name").start(job4Step1(stepBuilders)).next(job4Step2(stepBuilders)).on("FAILED")
            .end().from(job4Step2(stepBuilders)).on("*").to(job4Step3(stepBuilders)).end().build();
    }

    @Bean
    public Step job4Step1(StepBuilderFactory stepBuilders) {
        return stepBuilders.get("job4Step1Name").<Person, Person>chunk(10).reader(job4ItemReader())
            .writer(job4ItemWriter()).build();
    }

    @Bean
    public Step job4Step2(StepBuilderFactory stepBuilders) {
        return stepBuilders.get("job4Step2Name").<Person, Person>chunk(10).reader(job4ItemReader())
            .writer(job4ItemWriter()).build();
    }

    @Bean
    public Step job4Step3(StepBuilderFactory stepBuilders) {
        return stepBuilders.get("job4Step3Name").<Person, Person>chunk(10).reader(job4ItemReader())
            .writer(job4ItemWriter()).build();
    }

    private ItemReader<Person> job4ItemReader() {
        return new FlatFileItemReaderBuilder<Person>().name("job4ItemReader")
            .resource(new ClassPathResource("sourcefile/persons.csv")).delimited()
            .names(new String[] { "firstName", "lastName" }).targetType(Person.class).build();
    }

    private ItemWriter<Person> job4ItemWriter() {
        DelimitedLineAggregator<Person> delimitedLineAggregator = new DelimitedLineAggregator<>();
        BeanWrapperFieldExtractor<Person> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<>();
        beanWrapperFieldExtractor.setNames(new String[] { "firstName", "lastName" });
        delimitedLineAggregator.setDelimiter(",");
        delimitedLineAggregator.setFieldExtractor(beanWrapperFieldExtractor);

        return new FlatFileItemWriterBuilder<Person>().name("job4ItemWriter")
            .resource(new FileSystemResource("target/test-outputs/job4_output.csv"))
            .lineAggregator(delimitedLineAggregator).build();
    }
}