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
 * 多步骤串行执行
 * 执行顺序: job1Step1 --> job1Step2 --> job1Step3
 *
 * @author chenjun
 * @version V1.0
 * @since 2020-10-29 17:44
 */
@Configuration
public class Job1Config {

    @Bean
    public Job job1(JobBuilderFactory jobBuilders, StepBuilderFactory stepBuilders) {
        return jobBuilders.get("job1Name").start(job1Step1(stepBuilders)).next(job1Step2(stepBuilders))
            .next(job1Step3(stepBuilders)).build();
    }

    @Bean
    public Step job1Step1(StepBuilderFactory stepBuilders) {
        return stepBuilders.get("job1Step1Name").<Person, Person>chunk(10).reader(job1ItemReader())
            .writer(job1ItemWriter()).build();
    }

    @Bean
    public Step job1Step2(StepBuilderFactory stepBuilders) {
        return stepBuilders.get("job1Step2Name").<Person, Person>chunk(10).reader(job1ItemReader())
            .writer(job1ItemWriter()).build();
    }

    @Bean
    public Step job1Step3(StepBuilderFactory stepBuilders) {
        return stepBuilders.get("job1Step3Name").<Person, Person>chunk(10).reader(job1ItemReader())
            .writer(job1ItemWriter()).build();
    }

    /**
     * 此方法不添加 @Bean 注解的原因是，方法内没有使用到 spring 容器中的 bean 实例
     *
     * @return 读取器
     */
    private ItemReader<Person> job1ItemReader() {
        return new FlatFileItemReaderBuilder<Person>().name("job1ItemReader")
            .resource(new ClassPathResource("sourcefile/persons.csv")).delimited()
            .names(new String[] { "firstName", "lastName" }).targetType(Person.class).build();
    }

    private ItemWriter<Person> job1ItemWriter() {
        DelimitedLineAggregator<Person> delimitedLineAggregator = new DelimitedLineAggregator<>();
        BeanWrapperFieldExtractor<Person> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<>();
        beanWrapperFieldExtractor.setNames(new String[] { "firstName", "lastName" });
        delimitedLineAggregator.setDelimiter(",");
        delimitedLineAggregator.setFieldExtractor(beanWrapperFieldExtractor);

        return new FlatFileItemWriterBuilder<Person>().name("job1ItemWriter")
            .resource(new FileSystemResource("target/test-outputs/job1_output.csv"))
            .lineAggregator(delimitedLineAggregator).build();
    }

}