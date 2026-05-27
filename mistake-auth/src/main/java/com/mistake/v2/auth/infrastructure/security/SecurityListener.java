package com.mistake.v2.auth.infrastructure.security;

import cn.dev33.satoken.stp.StpInterface;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自定义权限校验监听器 — 实现 StpInterface，为 Sa-Token 提供角色与权限数据。
 *
 * <p>当前返回固定值，待数据库设计完成后改为从 DB 动态查询。
 *
 * @author mistake-team
 * @since 2.0.0
 */
@Component
public class SecurityListener implements StpInterface {

    /**
     * 根据 loginId 与 loginType 返回用户角色列表。
     *
     * @param loginId   登录账号 ID
     * @param loginType 账号类型
     * @return 角色编码集合
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // TODO: 从 DB 查询用户角色
        return SecurityConstant.DEFAULT_ROLES;
    }

    /**
     * 根据 loginId 与 loginType 返回用户权限码列表。
     *
     * @param loginId   登录账号 ID
     * @param loginType 账号类型
     * @return 权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // TODO: 从 DB 查询用户权限
        return SecurityConstant.DEFAULT_PERMISSIONS;
    }
}
