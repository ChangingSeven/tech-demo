package com.changing.springbatch.processor;

import com.changing.springbatch.model.Person;

import org.springframework.batch.item.ItemProcessor;

/**
 * @author chenjun
 * @version V1.0
 * @since 2020-10-29 17:47
 */
public class PersonItemProcessor implements ItemProcessor<Person, String> {

    @Override
    public String process(Person person) throws Exception {

        return "Hello " + person.getFirstName() + " " + person.getLastName() + "!";
    }

}