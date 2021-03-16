package com.changing.redis.mq.pubsub.configuration;

import com.changing.redis.mq.pubsub.anotation.RedisSubscriberMethod;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@ConditionalOnClass(value = RedisSubscriberTypeApplicationRunner.class)
@Slf4j
public class RedisSubscriberMethodCommandLineRunner implements CommandLineRunner {

    private final static String SCAN_PACKAGE = "com.changing.redis";

    @Autowired
    private RedisMessageListenerContainer redisMessageListenerContainer;
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void run(String... args) {
        /*
         * SubTypesScanner(false) 允许getAllTypes获取所有Object的子类, 不设置为false则 getAllTypes 会报错.默认为true.
         * MethodParameterNamesScanner 设置方法参数名称 扫描器,否则调用getConstructorParamNames 会报错
         * MethodAnnotationsScanner 设置方法注解 扫描器, 否则getConstructorsAnnotatedWith,getMethodsAnnotatedWith 会报错
         * MemberUsageScanner 设置 member 扫描器,否则 getMethodUsage 会报错, 不推荐使用,有可能会报错 Caused by: java.lang.ClassCastException: javassist.bytecode.InterfaceMethodrefInfo cannot be cast to javassist.bytecode.MethodrefInfo
         * TypeAnnotationsScanner 设置类注解 扫描器 ,否则 getTypesAnnotatedWith 会报错
         */
        List<MethodAnnotationsScanner> methodAnnotationsScanners = Collections.singletonList(new MethodAnnotationsScanner());
        Reflections reflections = new Reflections(SCAN_PACKAGE, methodAnnotationsScanners);
        Set<Method> redisSubscriberMethodSet = reflections.getMethodsAnnotatedWith(RedisSubscriberMethod.class);

        Optional.of(redisSubscriberMethodSet).ifPresent(methods -> methods.forEach(redisSubscriberMethod -> {
            Class<?> declaringClass = redisSubscriberMethod.getDeclaringClass();
            String classTypeName = declaringClass.getTypeName();
            String methodName = redisSubscriberMethod.getName();
            String listenerAdapterBeanName = methodName + "ListenerAdapter";

            Object subscriber = applicationContext.getBean(declaringClass);
            DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(MessageListenerAdapter.class);
            beanDefinitionBuilder.setScope(BeanDefinition.SCOPE_SINGLETON);
            beanDefinitionBuilder.addConstructorArgValue(subscriber);
            beanDefinitionBuilder.addConstructorArgValue(methodName);
            defaultListableBeanFactory.registerBeanDefinition(listenerAdapterBeanName, beanDefinitionBuilder.getBeanDefinition());
            log.info("Redis监听器注入Spring容器成功，beanName:{}, Class:{}", listenerAdapterBeanName, classTypeName);

            MessageListenerAdapter messageListenerAdapter = applicationContext.getBean(listenerAdapterBeanName, MessageListenerAdapter.class);
            RedisSubscriberMethod redisSubscriberMethodAnnotation = redisSubscriberMethod.getAnnotation(RedisSubscriberMethod.class);
            String topic = redisSubscriberMethodAnnotation.topic();
            PatternTopic patternTopic = new PatternTopic(topic);
            redisMessageListenerContainer.addMessageListener(messageListenerAdapter, patternTopic);

            log.info("Redis订阅方加入监听容器注入成功，Topic:{}，Class:{}，Method:{}", topic, classTypeName, methodName);
        }));
    }

}