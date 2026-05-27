package com.mistake.v2.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 统一响应体
 *
 * <p>所有接口统一使用此类包装返回结果，方便前端统一处理。
 *
 * @param <T> 响应数据类型
 * @author mistake-team
 * @since 2.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class R<T> {

    /** 状态码 */
    private int code;

    /** 返回消息 */
    private String message;

    /** 响应数据 */
    private T data;

    /** 时间戳 */
    private LocalDateTime timestamp;

    /**
     * 成功响应（带数据）
     *
     * @param data 响应数据
     * @param <T>  数据类型
     * @return R 实例
     */
    public static <T> R<T> ok(T data) {
        R<T> r = new R<>();
        r.setCode(200);
        r.setMessage("success");
        r.setData(data);
        r.setTimestamp(LocalDateTime.now());
        return r;
    }

    /**
     * 成功响应（无数据）
     *
     * @param <T> 数据类型
     * @return R 实例
     */
    public static <T> R<T> ok() {
        return ok(null);
    }

    /**
     * 失败响应
     *
     * @param code 错误码
     * @param msg  错误信息
     * @param <T>  数据类型
     * @return R 实例
     */
    public static <T> R<T> fail(int code, String msg) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMessage(msg);
        r.setData(null);
        r.setTimestamp(LocalDateTime.now());
        return r;
    }

    /**
     * 失败响应（基于错误码枚举）
     *
     * @param errorCode 错误码枚举
     * @param <T>       数据类型
     * @return R 实例
     */
    public static <T> R<T> fail(IErrorCode errorCode) {
        return fail(errorCode.getCode(), errorCode.getMessage());
    }

}
