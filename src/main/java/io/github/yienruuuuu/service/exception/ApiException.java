package io.github.yienruuuuu.service.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException{
    private final ErrorCode errorCode;
    private final Object data;

    // 基本建構子，只需提供錯誤代碼，使用預設錯誤訊息和 HTTP 狀態碼
    public ApiException(ErrorCode errorCode) {
        this(errorCode, errorCode.getMessage(), null);
    }

    // 帶有數據(data)的建構子
    public ApiException(ErrorCode errorCode, Object data) {
        this(errorCode, errorCode.getMessage(), data);
    }

    // 完整建構子，允許提供所有參數
    public ApiException(ErrorCode errorCode, String message, Object data) {
        super(message);
        this.errorCode = errorCode;
        this.data = data;
    }

}
