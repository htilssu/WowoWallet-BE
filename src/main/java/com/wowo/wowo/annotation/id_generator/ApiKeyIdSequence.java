package com.wowo.wowo.annotation.id_generator;

import com.wowo.wowo.generator.ApiKeyIdGenerator;
import org.hibernate.annotations.IdGeneratorType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@IdGeneratorType(ApiKeyIdGenerator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface ApiKeyIdSequence {

}
