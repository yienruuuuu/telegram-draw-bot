package io.github.yienruuuuu.config;

import io.github.yienruuuuu.service.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author Eric.Lee
 * Date: 2024/11/28
 */
@Aspect
@Component
@Slf4j
public class ServiceExceptionAspect {

    @AfterThrowing(pointcut = "execution(* io.github.yienruuuuu.service..*(..))", throwing = "ex")
    public void handleServiceException(JoinPoint joinPoint, Throwable ex) {
        if (ex instanceof ApiException apiException) {
            log.error("方法 {} 發生 ApiException：{} , data : {}", joinPoint.getSignature().toShortString(), apiException.getMessage(), apiException.getData());
        } else {
            log.error("方法 {} 發生 ApiException：{} ", joinPoint.getSignature().toShortString(), ex.getMessage(), ex);
        }
    }
}
