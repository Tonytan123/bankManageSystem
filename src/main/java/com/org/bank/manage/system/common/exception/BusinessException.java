package com.org.bank.manage.system.common.exception;

import com.org.bank.manage.system.common.ErrorCode;
import lombok.Getter;

/**
 * @author tucker
 */
@Getter
public class BusinessException extends RuntimeException {

    private final int code;

    private final String description;

    // 构造方法1：接收一个ErrorCode枚举
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    // 构造方法2：接收ErrorCode和自定义描述
    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    // 构造方法3：接收状态码、消息和描述
    public BusinessException(int code, String message, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }
}