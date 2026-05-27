# 错题本 2.0 — Mistake Book v2

> 基于 DDD 领域驱动设计 + Spring Boot 3.3 企业级重构  
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

## 📁 分层架构 (DDD)

```
com.mistake.v2
├── interfaces/          # 接口层
│   ├── controller/      # REST Controller
│   ├── dto/             # 视图对象 (VO)
│   └── assembler/       # Domain → VO 转换
├── application/         # 应用层
│   ├── service/         # ApplicationService
│   └── command/         # 命令对象 (DTO)
├── domain/              # 领域层
│   ├── mistake/         # 错题聚合
│   ├── review/          # 复习子域
│   │   └── algorithm/   # SM-2 间隔重复算法
│   └── shared/          # 共享值对象/枚举
└── infrastructure/      # 基础设施层
    ├── persistence/     # MyBatis-Plus 实现
    ├── security/        # Sa-Token + RBAC
    ├── config/          # Spring 配置
    └── common/          # 通用工具/异常/响应
```

## 🚀 快速启动

### 开发环境 (SQLite)

```bash
# 1. 克隆项目
git clone https://github.com/facilitates/mistake2.0.git
cd mistake2.0

# 2. 编译
mvn clean package -DskipTests

# 3. 启动 (dev 模式, 端口 8080)
java -jar target/mistake2.0-2.0.0-SNAPSHOT.jar --spring.profiles.active=dev

# 4. 访问
# API 文档: http://localhost:8080/api/v2/doc.html
# 默认账号: admin / 123456
```

### 生产环境 (MySQL + Redis)

```bash
# 配置环境变量
export MYSQL_HOST=127.0.0.1
export MYSQL_PASSWORD=your_password
export REDIS_PASSWORD=your_redis_pw

# 启动
java -jar app.jar --spring.profiles.active=prod
```

### Docker

```bash
docker build -t mistake2.0 .
docker run -d -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e MYSQL_HOST=host.docker.internal \
  mistake2.0
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
| 架构 | MVC 单体 | DDD 四层分离 |
| 鉴权 | JWT 手写 | Sa-Token RBAC |
| ORM | 原生 SQL | MyBatis-Plus |
| 异常 | try-catch 散落 | 全局异常处理器 |
| 响应 | 手动 JSON | 统一 R<T> |
| 配置 | 硬编码 | 多环境 + 环境变量 |
| 文档 | 无 | Knife4j OpenAPI |
| 部署 | systemd 脚本 | Docker + systemd |

## 📝 重构要点

- **零业务变更**: 所有原有功能完整保留，仅优化代码结构
- **高内聚低耦合**: 领域层零依赖框架，可独立单元测试
- **SM-2 算法**: 纯领域逻辑实现，无外部依赖
- **RBAC 即插即用**: Sa-Token 注解鉴权 + 角色权限动态加载
- **安全合规**: 参数校验 / SQL防注入 / 接口防刷预留 / 敏感日志脱敏

---

> 原 v1 项目: [mistake-book](https://github.com/facilitates/mistake-book)
