package com.mistake.v2.interfaces.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.mistake.v2.application.command.LoginCommand;
import com.mistake.v2.application.command.RegisterCommand;
import com.mistake.v2.common.response.R;
import com.mistake.v2.interfaces.dto.LoginVO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 认证控制器 — 处理用户登录、注册、登出
 *
 * @author mistake-team
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final AtomicLong USER_ID_GENERATOR = new AtomicLong(1);

    /**
     * 用户登录（当前硬编码 admin/123456）
     */
    @PostMapping("/login")
    public R<LoginVO> login(@Valid @RequestBody LoginCommand command) {
        if (!"admin".equals(command.getUsername()) || !"123456".equals(command.getPassword())) {
            return R.fail(401, "用户名或密码错误");
        }

        long userId = 1L;
        StpUtil.login(userId);

        LoginVO vo = LoginVO.builder()
                .token(StpUtil.getTokenValue())
                .userId(userId)
                .username(command.getUsername())
                .build();

        return R.ok(vo);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public R<LoginVO> register(@Valid @RequestBody RegisterCommand command) {
        // 简化注册：生成新用户ID并登录
        long userId = USER_ID_GENERATOR.incrementAndGet();
        StpUtil.login(userId);

        LoginVO vo = LoginVO.builder()
                .token(StpUtil.getTokenValue())
                .userId(userId)
                .username(command.getUsername())
                .build();

        return R.ok(vo);
    }

    /**
     * 登出
     */
    @PostMapping("/logout")
    public R<Void> logout() {
        StpUtil.logout();
        return R.ok();
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/me")
    public R<LoginVO> me() {
        if (!StpUtil.isLogin()) {
            return R.fail(401, "未登录");
        }

        long userId = StpUtil.getLoginIdAsLong();
        LoginVO vo = LoginVO.builder()
                .token(StpUtil.getTokenValue())
                .userId(userId)
                .username("admin")
                .build();

        return R.ok(vo);
    }
}
