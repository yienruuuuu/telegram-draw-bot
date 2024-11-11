package io.github.yienruuuuu.service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SysCode implements ErrorCode {

    //1000 success code
    OK(1000, "Success"),

    //2000 客戶端錯誤
    FAIL(2000, "Expected error"),
    BAD_CONFIG(2001, "Misconfiguration"),
    PARAMETER_ERROR(2002, "Parameter error"),
    NOT_REGISTER_ERROR(2003, "使用者尚未註冊"),

    //7000 資料錯誤
    NOT_FOUND(7000, "Data not found"),

    //9000 未知錯誤
    UNEXPECTED_ERROR(9999, "Unexpected error");

    private final Integer code;
    private final String message;

}