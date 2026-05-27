package com.mistake.v2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 错题本 2.0 — 基于 DDD + Spring Boot 3 企业级重构
 *
 * <p>分层架构：
 * <ul>
 *   <li>interfaces — 接口层（Controller / DTO / Assembler）</li>
 *   <li>application — 应用层（ApplicationService / Command）</li>
 *   <li>domain — 领域层（Aggregate / Entity / ValueObject / DomainService）</li>
 *   <li>infrastructure — 基础设施层（Persistence / Config / Common）</li>
 * </ul>
 *
 * <p>技术栈：Spring Boot 3.3 / Sa-Token RBAC / MyBatis-Plus / Redis / SQLite(dev) / MySQL(prod)
 *
 * @author mistake-team
 * @since 2.0.0
 */
@SpringBootApplication
public class MistakeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MistakeApplication.class, args);
    }
}
