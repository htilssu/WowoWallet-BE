package com.wowo.wowo.annotations.jpa;

import org.springframework.core.annotation.AliasFor;
import org.springframework.data.mongodb.core.index.HashIndexed;
import org.springframework.data.mongodb.core.index.Indexed;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Indexed
@HashIndexed
@Retention(RetentionPolicy.RUNTIME)
public @interface IndexAndHash {

    @AliasFor(annotation = Indexed.class, attribute = "name")
    String name() default "";
}