package com.ioio.jsontools.core.aspect.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class LogMethodCallAspect {

    private LoggerAdapter loggerAdapter;

    public LogMethodCallAspect(LoggerAdapter loggerAdapter) {
        this.loggerAdapter = loggerAdapter;
    }

    private void logMethodCall(JoinPoint joinPoint, LogMethodCall methodCallAnnotation) {
        if (methodCallAnnotation.before()) {
            loggerAdapter.log("--> Calling: "
                            + joinPoint.getSignature()
                            + " with params: "
                            + Arrays.stream(joinPoint.getArgs())
                            .map(object -> {
                                if (object != null) {
                                    return object.toString();
                                } else {
                                    return "null";
                                }
                            }).collect(Collectors.joining(" ")),
                    methodCallAnnotation.logLevel());
        }
    }
    private void logMethodReturn(JoinPoint joinPoint, LogMethodCall methodCallAnnotation, Object returnValue) {
        if (methodCallAnnotation.after()) {
            loggerAdapter.log("<-- Returning from "
                            + joinPoint.getSignature() +
                            ": " + returnValue.toString(),
                    methodCallAnnotation.logLevel());
        }
    }

    @Before("@annotation(methodAnnotation)")
    public void beforeMethodCall(JoinPoint joinPoint, LogMethodCall methodAnnotation) {
        logMethodCall(joinPoint, methodAnnotation);
    }

    @Before("@within(classAnnotation)")
    public void beforeClassMethodCall(JoinPoint joinPoint, LogMethodCall classAnnotation) {
        logMethodCall(joinPoint, classAnnotation);
    }

    @AfterReturning(value = "@annotation(methodAnnotation)", returning = "returnValue")
    public void afterMethodCall(JoinPoint joinPoint, LogMethodCall methodAnnotation, Object returnValue) {
        logMethodReturn(joinPoint, methodAnnotation, returnValue);
    }

    @AfterReturning(value = "@within(classAnnotation)", returning = "returnValue")
    public void afterClassMethodCall(JoinPoint joinPoint, LogMethodCall classAnnotation, Object returnValue) {
        logMethodReturn(joinPoint, classAnnotation, returnValue);
    }

}
