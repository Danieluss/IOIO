package com.ioio.jsontools.core.aspect.log;

import org.slf4j.event.Level;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface LogMethodCall {

    Level logLevel() default Level.INFO;

    boolean before() default true;

    boolean after() default true;
}
