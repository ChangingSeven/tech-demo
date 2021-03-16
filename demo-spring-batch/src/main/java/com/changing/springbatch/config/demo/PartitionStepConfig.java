package com.changing.springbatch.config.demo;

import com.changing.springbatch.model.Person;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.partition.support.SimplePartitioner;
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
 * 执行顺序: partitionJobStep1 --> partitionJobStep2 --> partitionJobStep3
 *
 * @author chenjun
 * @version V1.0
 * @since 2020-10-29 17:44
 */
@Configuration
public class PartitionStepConfig {

    @Bean
    public Job partitionJob(JobBuilderFactory jobBuilders, StepBuilderFactory stepBuilders) {
        return jobBuilders.get("partitionJobName").start(partitionJobStepManager(stepBuilders))
            .next(partitionJobStep2(stepBuilders)).next(partitionJobStep3(stepBuilders)).build();
    }

    @Bean
    public Step partitionJobStepManager(StepBuilderFactory stepBuilders) {
        return stepBuilders.get("partitionJobStepManagerName").<Person, Person>partitioner("10",
            new SimplePartitioner()).step(partitionJobStep2(stepBuilders)).gridSize(10).build();
    }

    @Bean
    public Step partitionJobStep2(StepBuilderFactory stepBuilders) {
        return stepBuilders.get("partitionJobStep2Name").<Person, Person>chunk(10).reader(partitionJobItemReader())
            .writer(partitionJobItemWriter()).build();
    }

    @Bean
    public Step partitionJobStep3(StepBuilderFactory stepBuilders) {
        return stepBuilders.get("partitionJobStep3Name").<Person, Person>chunk(10).reader(partitionJobItemReader())
            .writer(partitionJobItemWriter()).build();
    }

    /**
     * 此方法不添加 @Bean 注解的原因是，方法内没有使用到 spring 容器中的 bean 实例
     *
     * @return 读取器
     */
    private ItemReader<Person> partitionJobItemReader() {
        return new FlatFileItemReaderBuilder<Person>().name("partitionJobItemReader")
            .resource(new ClassPathResource("sourcefile/persons.csv")).delimited()
            .names(new String[] { "firstName", "lastName" }).targetType(Person.class).build();
    }

    private ItemWriter<Person> partitionJobItemWriter() {
        DelimitedLineAggregator<Person> delimitedLineAggregator = new DelimitedLineAggregator<>();
        BeanWrapperFieldExtractor<Person> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<>();
        beanWrapperFieldExtractor.setNames(new String[] { "firstName", "lastName" });
        delimitedLineAggregator.setDelimiter(",");
        delimitedLineAggregator.setFieldExtractor(beanWrapperFieldExtractor);

        return new FlatFileItemWriterBuilder<Person>().name("partitionJobItemWriter")
            .resource(new FileSystemResource("target/test-outputs/partitionJob_output.csv"))
            .lineAggregator(delimitedLineAggregator).build();
    }

}