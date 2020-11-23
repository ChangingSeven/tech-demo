package com.changing.springbatch.config.demo;

import com.changing.springbatch.model.Person;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * 读取文件并写入数据库
 *
 * @author chenjun
 * @version V1.0
 * @since 2020-11-20 09:45
 */
@Configuration
public class JdbcWriterJobConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public Job jdbcWriterJob(JobBuilderFactory jobBuilders, StepBuilderFactory stepBuilders) {
        return jobBuilders.get("jdbcWriterJobName").start(jdbcWriterJobStep1(stepBuilders)).build();
    }

    public Step jdbcWriterJobStep1(StepBuilderFactory stepBuilders) {
        return stepBuilders.get("jdbcWriterJobStep1Name").<Person, Person>chunk(10).reader(jdbcWriterJobItemReader())
            .writer(jdbcWriterJobItemWriter()).build();
    }

    private ItemReader jdbcWriterJobItemReader() {
        return new FlatFileItemReaderBuilder<Person>().name("jdbcWriterJobItemReader")
            .resource(new ClassPathResource("sourcefile/persons.csv")).delimited().names("firstName", "lastName")
            .targetType(Person.class).build();
    }

    private ItemWriter<Person> jdbcWriterJobItemWriter() {
        // values 中对应的是实体 Person 中的字段名
        String insertSql = "insert into person_tbl(first_name,last_name) values(:firstName,:lastName)";

        JdbcBatchItemWriter<Person> jdbcBatchItemWriter = new JdbcBatchItemWriterBuilder<Person>()
            .dataSource(dataSource).sql(insertSql)
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>()).build();
        jdbcBatchItemWriter.afterPropertiesSet();

        return jdbcBatchItemWriter;
    }

}