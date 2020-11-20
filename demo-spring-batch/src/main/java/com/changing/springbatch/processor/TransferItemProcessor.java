package com.changing.springbatch.processor;

import com.changing.springbatch.model.Person;

import org.springframework.batch.item.ItemProcessor;

/**
 * @author chenjun
 * @version V1.0
 * @since 2020-10-29 17:47
 */
public class TransferItemProcessor implements ItemProcessor<Person, Person> {

    @Override
    public Person process(Person person) throws Exception {

        // 返回 null 时 writer 不会将改条数据写入输出文件
        if ("陈".equals(person.getFirstName())) {
            return null;
        }

        person.setFirstName("姓：" + person.getFirstName());
        person.setLastName("名：" + person.getLastName());
        return person;
    }

}