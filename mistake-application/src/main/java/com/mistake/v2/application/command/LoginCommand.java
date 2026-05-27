package com.mistake.v2.application.command;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

/**
 * 登录命令 DTO
 *
 * @author mistake-team
 */
@Data
@Builder
public class LoginCommand {

    /** 用户名 */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /** 密码 */
    @NotBlank(message = "密码不能为空")
    private String password;
}
