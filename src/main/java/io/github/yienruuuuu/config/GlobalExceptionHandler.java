package io.github.yienruuuuu.config;


import io.github.yienruuuuu.bean.dto.ApiResponse;
import io.github.yienruuuuu.service.exception.ApiException;
import io.github.yienruuuuu.service.exception.SysCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Optional;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    ResponseEntity<ApiResponse> handleApiException(ApiException exception) {
        log.error("handleApiException", exception);
        return new ResponseEntity<>(
                new ApiResponse(
                        exception.getErrorCode().getCode(),
                        StringUtils.isBlank(exception.getMessage()) ? exception.getErrorCode().getMessage() : exception.getMessage(),
                        Optional.ofNullable(exception.getData()).orElse(new HashMap<String, String>())
                ),
                exception.getHttpStatus());
    }

    @ExceptionHandler(Throwable.class)
    ResponseEntity<ApiResponse> handleThrowable(Throwable throwable) {
        log.error("handleThrowable", throwable);
        return new ResponseEntity<>(
                new ApiResponse(
                        SysCode.UNEXPECTED_ERROR.getCode(),
                        SysCode.UNEXPECTED_ERROR.getMessage(),
                        null
                ),
                HttpStatus.EXPECTATION_FAILED);
    }
}