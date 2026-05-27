package com.mistake.v2.interfaces.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 登录/注册响应视图对象
 *
 * @author mistake-team
 */
@Data
@Builder
public class LoginVO {

    /** Sa-Token token */
    private String token;

    /** 用户ID */
    private Long userId;

    /** 用户名 */
    private String username;
}
