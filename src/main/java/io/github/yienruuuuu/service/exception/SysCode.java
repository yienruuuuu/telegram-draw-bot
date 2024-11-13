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
    PERMISSION_DENIED_ERROR(2004, "權限不足，禁止執行此操作"),

    //3000 系統錯誤
    CREATE_TEMPLATE_ERROR(3000, "建立模板失敗"),

    //7000 資料錯誤
    NOT_FOUND(7000, "Data not found"),
    ANNOUNCE_UNEXPECTED_ERROR(7001, "預期外錯誤發生，發生於文本查詢過程中"),
    RESOURCE_NOT_FOUNT(7002, "資源不存在"),

    //9000 未知錯誤
    UNEXPECTED_ERROR(9999, "Unexpected error");

    private final Integer code;
    private final String message;

}