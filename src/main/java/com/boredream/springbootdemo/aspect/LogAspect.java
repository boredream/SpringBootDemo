package com.boredream.springbootdemo.aspect;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Pointcut("execution(public * com.boredream.springbootdemo.controller.*.*(..))")
    public void webLog() {

    }

    @Around(value = "webLog()")
    public Object webLogAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // className的值：com.xx.controller.UserController
        String className = joinPoint.getTarget().getClass().getName();
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        // 方法名称示例：xx.controller.UserController.getUser():
        className = className.replace("com.boredream.springbootdemo.controller.", "");
        String methodName = className + "." + signature.getMethod().getName() + "():";

        //获取参数值
        Object[] args = joinPoint.getArgs();
        //获取参数名称
        String[] parameterNames = signature.getParameterNames();
        //参数值和参数名称是顺序是一致的
        Map<String, Object> map = new LinkedHashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            // 获取每个参数值
            if (Objects.nonNull(args[i])) {
                //过滤掉参数类型为 HttpServletResponse
                if (args[i] instanceof HttpServletResponse) {
                    continue;
                }
            }
            // 添加到LinkedHashMap中
            map.put(parameterNames[i], args[i]);
        }
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;
        log.info("[API] {} , duration = {}\n ==> request: {}\n <== response: {}",
                methodName, duration, new Gson().toJson(map), new Gson().toJson(proceed));
        return proceed;
    }
}
