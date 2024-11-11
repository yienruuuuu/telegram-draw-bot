package io.github.yienruuuuu.service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Getter
public class ApiException extends RuntimeException implements Serializable {
    private final ErrorCode errorCode;
    private final Object data;
    private final HttpStatus httpStatus;

    // 基本建構子，只需提供錯誤代碼，使用預設錯誤訊息和 HTTP 狀態碼
    public ApiException(ErrorCode errorCode) {
        this(errorCode, errorCode.getMessage(), HttpStatus.EXPECTATION_FAILED, null);
    }

    // 自定義錯誤訊息(message)的建構子
    public ApiException(ErrorCode errorCode, String message) {
        this(errorCode, message, HttpStatus.EXPECTATION_FAILED, null);
    }

    // 帶有數據(data)的建構子
    public ApiException(ErrorCode errorCode, Object data) {
        this(errorCode, errorCode.getMessage(), HttpStatus.EXPECTATION_FAILED, data);
    }

    // 完整建構子，允許提供所有參數
    public ApiException(ErrorCode errorCode, String message, HttpStatus httpStatus, Object data) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.data = data;
    }

}
