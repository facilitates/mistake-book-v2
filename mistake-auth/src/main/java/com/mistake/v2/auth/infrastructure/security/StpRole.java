package com.mistake.v2.auth.infrastructure.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 角色校验注解 — 标注在 Controller 方法上，校验当前登录用户是否拥有指定角色。
 *
 * <p>value 与 orRole 为 OR 关系，任一命中即通过。
 *
 * <pre>{@code
 * @StpRole("admin")
 * public Result<Void> delete(String id) { ... }
 *
 * @StpRole(value = "admin", orRole = {"superadmin"})
 * public Result<Void> batchDelete(List<String> ids) { ... }
 * }</pre>
 *
 * @author mistake-team
 * @since 2.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StpRole {

    /** 必须拥有的角色 */
    String value() default "";

    /** 可选角色列表（满足其一即可） */
    String[] orRole() default {};
}
