# 错题本 2.0 — Mistake Book v2

> 基于 DDD 领域驱动设计 + Spring Boot 3.3 多模块企业级架构  
> 原 Flask 1.x → Java 17 全栈重写

---

## 🏗 技术架构

| 层次 | 技术栈 |
|------|--------|
| **框架** | Spring Boot 3.3.5 / Spring MVC |
| **鉴权** | Sa-Token 1.38 + RBAC 角色权限模型 |
| **ORM** | MyBatis-Plus 3.5.7 + Druid 连接池 |
| **缓存** | Redis + Redisson |
| **数据库** | SQLite (dev) / MySQL 8 (prod) |
| **文档** | Knife4j (Swagger) |
| **工具** | Lombok / MapStruct / Hutool / Guava |
| **JDK** | 17+ |
| **构建** | Maven 多模块 |

## 📦 模块结构

```
mistake2.0/
├── pom.xml                      # 父 POM (依赖管理 + 版本统一)
├── mistake-common/              # 通用模块
│   └── 统一响应/异常/错误码/日志/脱敏
├── mistake-domain/              # 领域层 — 零框架依赖
│   └── 聚合根/值对象/枚举/仓储接口/SM-2算法
├── mistake-infrastructure/      # 基础设施层
│   └── MyBatis-Plus 持久化 / Sa-Token RBAC / Druid
├── mistake-application/         # 应用层
│   └── ApplicationService / Command / DTO
├── mistake-interfaces/          # 接口层
│   └── REST Controller / VO / Assembler
└── mistake-boot/                # 启动模块
    └── Spring Boot 入口 + 配置 + DB 迁移
```

### 模块依赖关系
```
mistake-boot
  ├── mistake-interfaces
  │     └── mistake-application
  │           └── mistake-domain
  └── mistake-infrastructure
        ├── mistake-domain
        └── mistake-common
```

## 🚀 快速启动

```bash
# 1. 编译全部模块
mvn clean package -DskipTests

# 2. 启动 (dev 模式, 端口 8080)
cd mistake-boot
java -jar target/mistake-boot-2.0.0-SNAPSHOT.jar --spring.profiles.active=dev

# 3. 访问
# API 文档: http://localhost:8080/api/v2/doc.html
# 默认账号: admin / 123456
```

### 单独编译某个模块
```bash
mvn clean compile -pl mistake-domain -am    # 编译 domain + 依赖
mvn clean test    -pl mistake-application    # 只跑应用层测试
```

## 📡 API 一览

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| POST | `/api/v2/auth/login` | 登录 | 无 |
| POST | `/api/v2/auth/register` | 注册 | 无 |
| GET | `/api/v2/auth/me` | 当前用户 | 登录 |
| POST | `/api/v2/mistakes` | 创建错题 | `mistake:write` |
| GET | `/api/v2/mistakes` | 错题列表 | 登录 |
| GET | `/api/v2/mistakes/{id}` | 错题详情 | 登录 |
| PUT | `/api/v2/mistakes/{id}` | 更新错题 | `mistake:write` |
| DELETE | `/api/v2/mistakes/{id}` | 删除错题 | `mistake:delete` |
| POST | `/api/v2/mistakes/{id}/review` | 复习评分 | 登录 |
| GET | `/api/v2/mistakes/review/due` | 待复习列表 | 登录 |
| GET | `/api/v2/mistakes/review/stats` | 复习统计 | 登录 |

## 🔐 RBAC 权限模型

```
角色 (Role)
  ├── admin     → 全部权限
  └── user      → mistake:read / mistake:write / review

权限 (Permission)
  ├── mistake:read    查看错题
  ├── mistake:write   管理错题
  ├── mistake:delete  删除错题
  ├── review          复习功能
  └── admin:all       系统管理
```

## 📊 重构对比

| 纬度 | v1 (Flask) | v2 (Spring Boot) |
|------|-----------|-------------------|
| 架构 | MVC 单体 | DDD 六模块分离 |
| 鉴权 | JWT 手写 | Sa-Token RBAC |
| ORM | 原生 SQL | MyBatis-Plus |
| 异常 | try-catch 散落 | 全局异常处理器 |
| 响应 | 手动 JSON | 统一 R&lt;T&gt; |
| 构建 | 无 | Maven 多模块 |
| 测试 | 无 | 模块独立可测 |
| 文档 | 无 | Knife4j OpenAPI |
| 部署 | systemd 脚本 | Docker + systemd |
