package com.changing.redis.mq.pubsub.anotation;


import javax.validation.constraints.NotNull;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisSubscriberMethod {

    /**
     * 消息topic
     *
     * @return
     */
    @NotNull
    String topic();
}