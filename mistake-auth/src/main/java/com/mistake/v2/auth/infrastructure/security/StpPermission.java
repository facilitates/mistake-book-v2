package com.mistake.v2.auth.infrastructure.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限校验注解 — 标注在 Controller 方法上，校验当前登录用户是否拥有指定权限码。
 *
 * <p>value 与 orPermission 为 OR 关系，任一命中即通过。
 *
 * <pre>{@code
 * @StpPermission("mistake:read")
 * public Result<List<MistakeVO>> list() { ... }
 *
 * @StpPermission(value = "admin:all", orPermission = {"mistake:write"})
 * public Result<Void> save(MistakeDTO dto) { ... }
 * }</pre>
 *
 * @author mistake-team
 * @since 2.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StpPermission {

    /** 必须拥有的权限码 */
    String value() default "";

    /** 可选权限码列表（满足其一即可） */
    String[] orPermission() default {};
}
