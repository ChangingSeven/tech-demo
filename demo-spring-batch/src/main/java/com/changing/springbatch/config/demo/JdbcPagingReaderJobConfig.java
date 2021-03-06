package com.changing.springbatch.config.demo;

import com.changing.springbatch.model.Person;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
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
 * @author chenjun
 * @version V1.0
 * @since 2020-11-20 09:45
 */
@Configuration
public class JdbcPagingReaderJobConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public Job jdbcPagingJobReader(JobBuilderFactory jobBuilders, StepBuilderFactory stepBuilders) throws Exception {
        return jobBuilders.get("jdbcPagingJobReaderName").start(jdbcPagingJobReaderStep1(stepBuilders)).build();
    }

    public Step jdbcPagingJobReaderStep1(StepBuilderFactory stepBuilders) throws Exception {
        return stepBuilders.get("jdbcPagingJobReaderStep1Name").<Person, Person>chunk(10).reader(jdbcPagingJobReaderItemReader())
            .writer(jdbcPagingJobReaderItemWriter()).build();
    }

    private JdbcPagingItemReader jdbcPagingJobReaderItemReader() throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", "2");

        JdbcPagingItemReader<Person> jdbcPagingJobReaderItemReader = new JdbcPagingItemReaderBuilder<Person>()
            .name("jdbcPagingJobReaderItemReader").dataSource(dataSource).queryProvider(queryProvider().getObject())
            .parameterValues(paramMap).rowMapper(new PersonRowMapper()).pageSize(2).build();
        jdbcPagingJobReaderItemReader.afterPropertiesSet();

        return jdbcPagingJobReaderItemReader;
    }

    private ItemWriter<Person> jdbcPagingJobReaderItemWriter() {
        DelimitedLineAggregator<Person> delimitedLineAggregator = new DelimitedLineAggregator<>();
        BeanWrapperFieldExtractor<Person> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<>();
        beanWrapperFieldExtractor.setNames(new String[] { "firstName", "lastName" });
        delimitedLineAggregator.setDelimiter(",");
        delimitedLineAggregator.setFieldExtractor(beanWrapperFieldExtractor);

        return new FlatFileItemWriterBuilder<Person>().name("jdbcPagingJobReaderItemWriter")
            .resource(new FileSystemResource("target/test-outputs/jdbcPagingJobReader_output.csv"))
            .lineAggregator(delimitedLineAggregator).build();
    }

    public SqlPagingQueryProviderFactoryBean queryProvider() {
        SqlPagingQueryProviderFactoryBean provider = new SqlPagingQueryProviderFactoryBean();

        provider.setDataSource(dataSource);
        provider.setSelectClause("select id,first_name,last_name");
        provider.setFromClause("from person_tbl");
        provider.setWhereClause("where id > :id");
        provider.setSortKey("id");

        return provider;
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