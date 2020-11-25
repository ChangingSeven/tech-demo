package com.changing.springbatch.config.demo;

import com.changing.springbatch.model.Trade;
import com.changing.springbatch.processor.TradeProcessor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
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
            .processor(new TradeProcessor()).writer(xmlJobItemWriter()).build();
    }

    private StaxEventItemReader xmlJobItemReader() {
        return new StaxEventItemReaderBuilder<Trade>().name("xmlJobItemReader")
            .resource(new ClassPathResource("sourcefile/xml_reader.xml"))
            // 单条数据的节点标识
            .addFragmentRootElements("trade").unmarshaller(tradeMarshaller()).build();
    }

    private StaxEventItemWriter xmlJobItemWriter() {
        return new StaxEventItemWriterBuilder<Trade>().name("xmlJobItemWriter").marshaller(tradeMarshaller())
            .resource(new FileSystemResource("target/test-outputs/xmljob_output.xml"))
            // 定义根节点的名字
            .rootTagName("tradeList").overwriteOutput(true).build();

    }

    private XStreamMarshaller tradeMarshaller() {

        // 设置读取数据时映射字段类型
        Map<String, Class> aliases = new HashMap<>();
        aliases.put("trade", Trade.class);
        aliases.put("price", BigDecimal.class);
        aliases.put("isin", String.class);
        aliases.put("customer", String.class);
        aliases.put("quantity", Long.class);
        aliases.put("priceCustomer", List.class);
        aliases.put("stringProp", String.class);

        // 设置写入数据时，实体类中字段与 xml 中节点的名称映射关系
        // 没有指定输入映射节点名的字段，默认使用字段名作为节点名
        // 前面的 key 是完整类路径加字段名，后面的 value 是最终输出到文件里的节点名
        Map<String, String> filedAliases = new HashMap<>();
        filedAliases.put("com.changing.springbatch.model.Trade.price", "PRICE");
        // TODO 写入xml时List<String>中的节点名如何定义，暂未找到解决方式
        filedAliases.put("com.changing.springbatch.model.Trade.stringProp", "aaaa");

        XStreamMarshaller marshaller = new XStreamMarshaller();
        marshaller.setAliases(aliases);
        marshaller.setFieldAliases(filedAliases);

        return marshaller;
    }
}