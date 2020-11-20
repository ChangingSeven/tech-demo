package com.changing.springbatch.config.demo;

import com.changing.springbatch.model.Trade;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

/**
 * 读取xml
 *
 * @author chenjun
 * @version V1.0
 * @since 2020-11-18 09:36
 */
@Configuration
public class XmlJobConfig {

    @Bean
    public Job xmlJob(JobBuilderFactory jobBuilders, StepBuilderFactory stepBuilders) {
        return jobBuilders.get("xmlJobName").start(xmlJobStep(stepBuilders)).build();
    }

    @Bean
    public Step xmlJobStep(StepBuilderFactory stepBuilders) {
        return stepBuilders.get("xmlJobStepName").<Trade, Trade>chunk(10).reader(xmlJobItemReader())
            .writer(xmlJobItemWriter()).build();
    }

    private StaxEventItemReader xmlJobItemReader() {
        return new StaxEventItemReaderBuilder<Trade>().name("xmlJobItemReader")
            .resource(new ClassPathResource("sourcefile/xml_reader.xml")).addFragmentRootElements("trade")
            .unmarshaller(tradeMarshaller()).build();
    }

    private StaxEventItemWriter xmlJobItemWriter() {
        return new StaxEventItemWriterBuilder<Trade>().name("xmlJobItemWriter").marshaller(tradeMarshaller())
            .resource(new FileSystemResource("target/test-outputs/xmljob_output.xml")).rootTagName("trade")
            .overwriteOutput(true).build();

    }

    private XStreamMarshaller tradeMarshaller() {
        Map<String, Class> aliases = new HashMap<>();

        aliases.put("trade", Trade.class);
        aliases.put("price", BigDecimal.class);
        aliases.put("isin", String.class);
        aliases.put("customer", String.class);
        aliases.put("quantity", Long.class);

        XStreamMarshaller marshaller = new XStreamMarshaller();
        marshaller.setAliases(aliases);

        return marshaller;
    }
}