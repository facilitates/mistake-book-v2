package com.mistake.v2.infrastructure.security;

import java.util.List;

/**
 * RBAC 角色与权限常量定义
 *
 * @author mistake-team
 * @since 2.0.0
 */
public final class SecurityConstant {

    private SecurityConstant() {
    }

    // ── 角色 ──

    /** 超级管理员 */
    public static final String ROLE_ADMIN = "admin";

    /** 普通用户 */
    public static final String ROLE_USER = "user";

    // ── 权限 ──

    /** 错题：读 */
    public static final String PERM_MISTAKE_READ = "mistake:read";

    /** 错题：写 */
    public static final String PERM_MISTAKE_WRITE = "mistake:write";

    /** 错题：删除 */
    public static final String PERM_MISTAKE_DELETE = "mistake:delete";

    /** 审核 */
    public static final String PERM_REVIEW = "review";

    /** 管理员全权限 */
    public static final String PERM_ADMIN = "admin:all";

    // ── 列表常量 ──

    /** 默认角色列表 */
    public static final List<String> DEFAULT_ROLES = List.of(ROLE_USER);

    /** 默认权限列表 */
    public static final List<String> DEFAULT_PERMISSIONS = List.of(PERM_MISTAKE_READ, PERM_MISTAKE_WRITE);

    /** 管理员全部权限 */
    public static final List<String> ADMIN_PERMISSIONS = List.of(
            PERM_MISTAKE_READ,
            PERM_MISTAKE_WRITE,
            PERM_MISTAKE_DELETE,
            PERM_REVIEW,
            PERM_ADMIN
    );
}
