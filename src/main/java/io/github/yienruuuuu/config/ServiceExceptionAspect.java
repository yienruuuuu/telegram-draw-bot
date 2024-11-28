package io.github.yienruuuuu.config;

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
        log.error("方法 {} 發生異常：{}", joinPoint.getSignature().toShortString(), ex.getMessage(), ex);
    }
}
