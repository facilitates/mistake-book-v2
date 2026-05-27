package com.mistake.v2.infrastructure.common.exception;

import com.mistake.v2.infrastructure.common.response.IErrorCode;

/**
 * 业务异常
 *
 * <p>用于业务逻辑中主动抛出的异常，由全局异常处理器统一捕获并返回给前端。
 *
 * @author mistake-team
 * @since 2.0.0
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** 错误码 */
    private final int code;

    /** 错误信息 */
    private final String message;

    /**
     * 基于错误码枚举构造
     *
     * @param errorCode 错误码枚举
     */
    public BusinessException(IErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    /**
     * 基于自定义 code 和 msg 构造
     *
     * @param code 错误码
     * @param msg  错误信息
     */
    public BusinessException(int code, String msg) {
        super(msg);
        this.code = code;
        this.message = msg;
    }

    /**
     * 基于错误信息构造，默认错误码 1001（业务异常）
     *
     * @param msg 错误信息
     */
    public BusinessException(String msg) {
        super(msg);
        this.code = 1001;
        this.message = msg;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
