package com.changing.springbatch.config;

import com.changing.springbatch.model.Person;
import com.changing.springbatch.processor.TransferItemProcessor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 应用启动时动态加载 step、job，当前是写死的，可以调整为数据库读取配置的方式
 *
 * @author chenjun
 * @version V1.0
 * @since 2020-11-16 11:20
 */
@Component
@Slf4j
public class JobAutoRegistryConfiguration implements CommandLineRunner {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private JobBuilderFactory jobBuilders;
    @Autowired
    private StepBuilderFactory stepBuilders;

    @Override
    public void run(String... args) throws Exception {
        ConfigurableApplicationContext applicationContext1 = (ConfigurableApplicationContext) applicationContext;

        Job serialJob = buildSerialJob();
        String serialJobName = serialJob.getName();
        applicationContext1.getBeanFactory().registerSingleton(serialJobName, serialJob);
        log.info("串行批处理任务{}注入成功", serialJobName);

        Job parallelJob = buildParallelJob();
        String parallelJobName = parallelJob.getName();
        applicationContext1.getBeanFactory().registerSingleton(parallelJobName, parallelJob);
        log.info("并行批处理任务{}注入成功", parallelJobName);
    }

    /**
     * 构建串行批处理任务
     *
     * @return 批处理任务
     */
    private Job buildSerialJob() {
        DelimitedLineAggregator<Person> delimitedLineAggregator = new DelimitedLineAggregator<>();
        BeanWrapperFieldExtractor<Person> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<>();
        beanWrapperFieldExtractor.setNames(new String[] { "firstName", "lastName" });
        delimitedLineAggregator.setDelimiter(",");
        delimitedLineAggregator.setFieldExtractor(beanWrapperFieldExtractor);

        // 步骤一：csv文件内容写入csv
        FlatFileItemReader<Person> step01ItemReader = new FlatFileItemReaderBuilder<Person>().name("step01ItemReader")
            .resource(new ClassPathResource("csv/persons.csv")).delimited()
            .names(new String[] { "firstName", "lastName" }).targetType(Person.class).build();
        FlatFileItemWriter<Person> step01ItemWriter = new FlatFileItemWriterBuilder<Person>().name("step01ItemWriter")
            .resource(new FileSystemResource("target/test-outputs/serial_step01_output.csv"))
            .lineAggregator(delimitedLineAggregator).build();
        TransferItemProcessor step01ItemProcessor = new TransferItemProcessor();
        TaskletStep step01 = stepBuilders.get("serialJobStep01").<Person, Person>chunk(10).reader(step01ItemReader)
            .processor(step01ItemProcessor).writer(step01ItemWriter).build();

        // 步骤二：csv文件内容写入csv
        FlatFileItemReader<Person> step02ItemReader = new FlatFileItemReaderBuilder<Person>().name("step02ItemReader")
            .resource(new FileSystemResource("target/test-outputs/serial_step01_output.csv")).delimited()
            .names(new String[] { "firstName", "lastName" }).targetType(Person.class).build();
        FlatFileItemWriter<Person> step02ItemWriter = new FlatFileItemWriterBuilder<Person>().name("step02ItemWriter")
            .resource(new FileSystemResource("target/test-outputs/serial_step02_output.csv"))
            .lineAggregator(delimitedLineAggregator).build();
        TransferItemProcessor step02ItemProcessor = new TransferItemProcessor();
        TaskletStep step02 = stepBuilders.get("serialJobStep02").<Person, Person>chunk(10).reader(step02ItemReader)
            .processor(step02ItemProcessor).writer(step02ItemWriter).build();

        // 步骤三：csv文件内容写入csv
        FlatFileItemReader<Person> step03ItemReader = new FlatFileItemReaderBuilder<Person>().name("step03ItemReader")
            .resource(new FileSystemResource("target/test-outputs/serial_step02_output.csv")).delimited()
            .names(new String[] { "firstName", "lastName" }).targetType(Person.class).build();
        FlatFileItemWriter<Person> step03ItemWriter = new FlatFileItemWriterBuilder<Person>().name("step03ItemWriter")
            .resource(new FileSystemResource("target/test-outputs/serial_step03_output.csv"))
            .lineAggregator(delimitedLineAggregator).build();
        TransferItemProcessor step03ItemProcessor = new TransferItemProcessor();
        TaskletStep step03 = stepBuilders.get("serialJobStep03").<Person, Person>chunk(10).reader(step03ItemReader)
            .processor(step03ItemProcessor).writer(step03ItemWriter).build();

        return jobBuilders.get("serialJob").start(step01).next(step02).next(step03).build();
    }

    /**
     * 构建并行批处理任务
     *
     * @return 批处理任务
     */
    private Job buildParallelJob() {
        DelimitedLineAggregator<Person> delimitedLineAggregator = new DelimitedLineAggregator<>();
        BeanWrapperFieldExtractor<Person> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<>();
        beanWrapperFieldExtractor.setNames(new String[] { "firstName", "lastName" });
        delimitedLineAggregator.setDelimiter(",");
        delimitedLineAggregator.setFieldExtractor(beanWrapperFieldExtractor);

        // 步骤一：csv文件内容写入csv
        FlatFileItemReader<Person> step11ItemReader = new FlatFileItemReaderBuilder<Person>().name("step11ItemReader")
            .resource(new ClassPathResource("csv/persons.csv")).delimited()
            .names(new String[] { "firstName", "lastName" }).targetType(Person.class).build();
        FlatFileItemWriter<Person> step11ItemWriter = new FlatFileItemWriterBuilder<Person>().name("step11ItemWriter")
            .resource(new FileSystemResource("target/test-outputs/parallel_step11_output.csv"))
            .lineAggregator(delimitedLineAggregator).build();
        TransferItemProcessor step11ItemProcessor = new TransferItemProcessor();
        TaskletStep step11 = stepBuilders.get("parallelJobStep11").<Person, Person>chunk(10).reader(step11ItemReader)
            .processor(step11ItemProcessor).writer(step11ItemWriter).build();

        // 步骤二：csv文件内容写入csv
        FlatFileItemReader<Person> step12ItemReader = new FlatFileItemReaderBuilder<Person>().name("step12ItemReader")
            .resource(new FileSystemResource("target/test-outputs/parallel_step11_output.csv")).delimited()
            .names(new String[] { "firstName", "lastName" }).targetType(Person.class).build();
        FlatFileItemWriter<Person> step12ItemWriter = new FlatFileItemWriterBuilder<Person>().name("step12ItemWriter")
            .resource(new FileSystemResource("target/test-outputs/parallel_step12_output.csv"))
            .lineAggregator(delimitedLineAggregator).build();
        TransferItemProcessor step12ItemProcessor = new TransferItemProcessor();
        TaskletStep step12 = stepBuilders.get("parallelJobStep12").<Person, Person>chunk(10).reader(step12ItemReader)
            .processor(step12ItemProcessor).writer(step12ItemWriter).build();

        // 步骤三：csv文件内容写入csv
        FlatFileItemReader<Person> step13ItemReader = new FlatFileItemReaderBuilder<Person>().name("step13ItemReader")
            .resource(new FileSystemResource("target/test-outputs/parallel_step11_output.csv")).delimited()
            .names(new String[] { "firstName", "lastName" }).targetType(Person.class).build();
        FlatFileItemWriter<Person> step13ItemWriter = new FlatFileItemWriterBuilder<Person>().name("step13ItemWriter")
            .resource(new FileSystemResource("target/test-outputs/parallel_step13_output.csv"))
            .lineAggregator(delimitedLineAggregator).build();
        TransferItemProcessor step13ItemProcessor = new TransferItemProcessor();
        TaskletStep step13 = stepBuilders.get("parallelJobStep13").<Person, Person>chunk(10).reader(step13ItemReader)
            .processor(step13ItemProcessor).writer(step13ItemWriter).build();

        // 步骤四：csv文件内容写入csv
        FlatFileItemReader<Person> step14ItemReader = new FlatFileItemReaderBuilder<Person>().name("step14ItemReader")
            .resource(new FileSystemResource("target/test-outputs/parallel_step12_output.csv")).delimited()
            .names(new String[] { "firstName", "lastName" }).targetType(Person.class).build();
        FlatFileItemWriter<Person> step14ItemWriter = new FlatFileItemWriterBuilder<Person>().name("step14ItemWriter")
            .resource(new FileSystemResource("target/test-outputs/parallel_step14_output.csv"))
            .lineAggregator(delimitedLineAggregator).build();
        TransferItemProcessor step14ItemProcessor = new TransferItemProcessor();
        TaskletStep step14 = stepBuilders.get("parallelJobStep14").<Person, Person>chunk(10).reader(step14ItemReader)
            .processor(step14ItemProcessor).writer(step14ItemWriter).build();

        Flow flow01 = new FlowBuilder<SimpleFlow>("parallelFlow01").start(step12).build();
        Flow flow02 = new FlowBuilder<SimpleFlow>("parallelFlow02").start(step13).build();
        Flow flow03 = new FlowBuilder<SimpleFlow>("parallelFlow03").start(flow01).split(new SimpleAsyncTaskExecutor())
            .add(flow02).build();
        SimpleFlow flow4 = new FlowBuilder<SimpleFlow>("parallelFlow04").start(step11).next(flow03).build();

        return jobBuilders.get("parallelJob").start(flow4).next(step14).end().build();
    }

}