package com.mistake.v2.common.enums;

import com.mistake.v2.common.response.IErrorCode;

/**
 * 统一错误码枚举
 *
 * <p>实现 {@link IErrorCode} 接口，覆盖常用 HTTP 状态码及业务错误码。
 *
 * @author mistake-team
 * @since 2.0.0
 */
public enum ErrorCode implements IErrorCode {

    /** 成功 */
    SUCCESS(200, "成功"),

    /** 参数错误 */
    BAD_REQUEST(400, "参数错误"),

    /** 未登录 */
    UNAUTHORIZED(401, "未登录"),

    /** 无权限 */
    FORBIDDEN(403, "无权限"),

    /** 资源不存在 */
    NOT_FOUND(404, "资源不存在"),

    /** 服务器内部错误 */
    INTERNAL_ERROR(500, "服务器内部错误"),

    /** 业务异常 */
    BUSINESS_ERROR(1001, "业务异常"),

    /** 请求过于频繁 */
    RATE_LIMIT(1002, "请求过于频繁");

    private final int code;

    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
