package com.changing.springbatch.config.demo;

import com.changing.springbatch.model.Person;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

/**
 * 数据库读取写入文件
 *
 * @author chenjun
 * @version V1.0
 * @since 2020-11-20 09:45
 */
@Configuration
public class JdbcReaderJobConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public Job jdbcJobReader(JobBuilderFactory jobBuilders, StepBuilderFactory stepBuilders) {
        return jobBuilders.get("jdbcJobReaderName").start(jdbcJobReaderStep1(stepBuilders)).build();
    }

    public Step jdbcJobReaderStep1(StepBuilderFactory stepBuilders) {
        return stepBuilders.get("jdbcJobReaderStep1Name").<Person, Person>chunk(10).reader(jdbcJobReaderItemReader())
            .writer(jdbcJobReaderItemWriter()).build();
    }

    private JdbcCursorItemReader jdbcJobReaderItemReader() {
        String querySql = "select first_name,last_name from person_tbl";

        return new JdbcCursorItemReaderBuilder<Person>().name("jdbcJobReaderItemReader").dataSource(dataSource).sql(querySql)
            .rowMapper(new PersonRowMapper()).build();
    }

    private ItemWriter<Person> jdbcJobReaderItemWriter() {
        DelimitedLineAggregator<Person> delimitedLineAggregator = new DelimitedLineAggregator<>();
        BeanWrapperFieldExtractor<Person> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<>();
        beanWrapperFieldExtractor.setNames(new String[] { "firstName", "lastName" });
        delimitedLineAggregator.setDelimiter(",");
        delimitedLineAggregator.setFieldExtractor(beanWrapperFieldExtractor);

        return new FlatFileItemWriterBuilder<Person>().name("jdbcJobReaderItemWriter")
            .resource(new FileSystemResource("target/test-outputs/jdbcJobReader_output.csv"))
            .lineAggregator(delimitedLineAggregator).build();
    }

    public class PersonRowMapper implements RowMapper<Person> {

        public static final String FIRST_NAME = "first_name";
        public static final String LAST_NAME = "last_name";

        @Nullable
        @Override
        public Person mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            Person person = new Person();

            person.setFirstName(resultSet.getString(FIRST_NAME));
            person.setLastName(resultSet.getString(LAST_NAME));

            return person;
        }
    }

}