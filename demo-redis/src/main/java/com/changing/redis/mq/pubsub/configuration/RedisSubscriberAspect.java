package com.changing.redis.mq.pubsub.configuration;

import com.changing.redis.mq.pubsub.anotation.RedisSubscriberMethod;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class RedisSubscriberAspect {

    @Autowired
    private RedisMessageListenerContainer redisMessageListenerContainer;

    @Autowired
    private ApplicationContext applicationContext;

    @Pointcut(value = "@annotation(com.changing.redis.mq.pubsub.anotation.RedisSubscriberMethod)")
    public void redisSubscriberMethodPoint() {
    }

    @Around("redisSubscriberMethodPoint()")
    public void redisSubscriberMethodAdvice(ProceedingJoinPoint joinPoint) {
        Class<?> aClass = joinPoint.getThis().getClass();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        MessageListenerAdapter refreshCacheMessage = new MessageListenerAdapter(aClass, methodSignature.getMethod().getName());

        RedisSubscriberMethod redisSubscriberMethodAnnotation = aClass.getAnnotation(RedisSubscriberMethod.class);
        PatternTopic patternTopic = new PatternTopic(redisSubscriberMethodAnnotation.topic());
        redisMessageListenerContainer.addMessageListener(refreshCacheMessage, patternTopic);
    }

    @Pointcut(value = "@annotation(com.changing.redis.mq.pubsub.anotation.RedisSubscriberType)")
    public void redisSubscriberTypePoint() {
    }

    @Around("redisSubscriberTypePoint()")
    public void redisSubscriberTypeAdvice(ProceedingJoinPoint joinPoint) {
        Class<?> aClass = joinPoint.getThis().getClass();

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(aClass);
//        beanDefinitionBuilder.addPropertyValue()

    }
}