package com.nowcoder.community.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AlphaAspect {

    //定义切入点
//    @Pointcut("execution(* com.nowcoder.community.service.*.*(..))")
//    public void pointcut() {
//
//    }

//    //定义通知
//    @Before("pointcut()")
//    public void before() {
//        System.out.println("before");
//    }
//
//    @AfterReturning("pointcut()")
//    public void afterReturning() {
//        System.out.println("afterReturning");
//    }
//
//    @Around("pointcut()")
//    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
//        System.out.println("around before");
//        Object obj = joinPoint.proceed();
//        System.out.println("around after");
//        return obj;
//    }
}
