package com.mistake.v2.infrastructure.common.response;

/**
 * 错误码接口
 *
 * <p>所有错误码枚举必须实现此接口，确保对外暴露统一的 code 和 message。
 *
 * @author mistake-team
 * @since 2.0.0
 */
public interface IErrorCode {

    /**
     * 获取错误码
     *
     * @return 错误码
     */
    int getCode();

    /**
     * 获取错误信息
     *
     * @return 错误信息
     */
    String getMessage();

}
