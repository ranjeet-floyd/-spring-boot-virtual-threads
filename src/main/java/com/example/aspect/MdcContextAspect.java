package com.example.aspect;

import com.example.utils.MdcContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import java.util.Map;

@Aspect
@Component
public class MdcContextAspect {

    @Around("@annotation(org.springframework.scheduling.annotation.Async)")
    public Object aroundAsyncMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Map<String, String> contextMap = MdcContextHolder.getMdcContext();
        try {
            MdcContextHolder.setMdcContext(contextMap);
            return joinPoint.proceed();
        } finally {
            MdcContextHolder.clearMdcContext();
        }
    }
}
