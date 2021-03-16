package com.changing.redis.mq.pubsub.configuration;

import com.changing.redis.mq.pubsub.anotation.RedisSubscriberType;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

@Component
@Slf4j
public class RedisSubscriberTypeApplicationRunner implements ApplicationRunner {

    private final static String SCAN_PACKAGE = "com.changing.redis";

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private CountDownLatch countDownLatch;

    @Override
    public void run(ApplicationArguments args) {
        Reflections reflections = new Reflections(SCAN_PACKAGE);
        Set<Class<?>> redisSubscriberTypeSet = reflections.getTypesAnnotatedWith(RedisSubscriberType.class);

        Optional.of(redisSubscriberTypeSet).ifPresent(classes -> classes.forEach(redisSubscriberClass -> {
            String classSimpleName = redisSubscriberClass.getSimpleName();
            String classTypeName = redisSubscriberClass.getTypeName();
            String beanName = classSimpleName.substring(0, 1).toLowerCase() + classSimpleName.substring(1);
            DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(redisSubscriberClass);
            beanDefinitionBuilder.setScope(BeanDefinition.SCOPE_SINGLETON);
            beanDefinitionBuilder.addConstructorArgValue(countDownLatch);
            defaultListableBeanFactory.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());

            log.info("订阅方注入Spring容器成功, beanName:{}, classTypeName:{}", beanName, classTypeName);
        }));
    }

}