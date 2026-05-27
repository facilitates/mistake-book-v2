-- ============================================================
-- 错题本 2.0 — 数据库迁移脚本 (V1)
-- 兼容 SQLite (dev) 与 MySQL (prod)
-- ============================================================

-- 错题表
CREATE TABLE IF NOT EXISTS mistakes (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,  -- MySQL: BIGINT AUTO_INCREMENT
    user_id         BIGINT       NOT NULL,
    subject         VARCHAR(32)  NOT NULL DEFAULT '高等数学',
    topic           VARCHAR(64)  NOT NULL DEFAULT '',
    question_type   VARCHAR(16)  NOT NULL DEFAULT 'calculation',
    difficulty      TINYINT      NOT NULL DEFAULT 3 CHECK(difficulty BETWEEN 1 AND 5),
    image_path      TEXT,
    problem_text    TEXT,
    solution        TEXT,
    answer          VARCHAR(512),
    knowledge_points TEXT,                              -- JSON array
    mastery         VARCHAR(16)  NOT NULL DEFAULT 'new', -- new / reviewing / mastered
    review_interval INT          NOT NULL DEFAULT 0,
    ease_factor     REAL         NOT NULL DEFAULT 2.5,
    review_count    INT          NOT NULL DEFAULT 0,
    next_review_at  DATE,
    created_at      DATETIME     NOT NULL DEFAULT (datetime('now','localtime')),
    updated_at      DATETIME     NOT NULL DEFAULT (datetime('now','localtime')),
    deleted         TINYINT      NOT NULL DEFAULT 0
);

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    username      VARCHAR(64)  NOT NULL UNIQUE,
    password      VARCHAR(256) NOT NULL,                -- BCrypt 加密
    email         VARCHAR(128),
    nickname      VARCHAR(64),
    avatar        VARCHAR(512),
    status        TINYINT      NOT NULL DEFAULT 1,      -- 1:正常 0:禁用
    created_at    DATETIME     NOT NULL DEFAULT (datetime('now','localtime')),
    updated_at    DATETIME     NOT NULL DEFAULT (datetime('now','localtime'))
);

-- 角色表
CREATE TABLE IF NOT EXISTS roles (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    name        VARCHAR(32)  NOT NULL UNIQUE,
    code        VARCHAR(32)  NOT NULL UNIQUE,           -- admin / user
    description VARCHAR(256)
);

-- 权限表
CREATE TABLE IF NOT EXISTS permissions (
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    name        VARCHAR(64)  NOT NULL UNIQUE,
    code        VARCHAR(64)  NOT NULL UNIQUE,           -- mistake:read / mistake:write
    description VARCHAR(256)
);

-- 用户-角色关联
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id)
);

-- 角色-权限关联
CREATE TABLE IF NOT EXISTS role_permissions (
    role_id       BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, permission_id)
);

-- 复习会话表
CREATE TABLE IF NOT EXISTS review_sessions (
    id             INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id        BIGINT   NOT NULL,
    cards_reviewed INT      NOT NULL DEFAULT 0,
    cards_mastered INT      NOT NULL DEFAULT 0,
    started_at     DATETIME NOT NULL DEFAULT (datetime('now','localtime')),
    ended_at       DATETIME
);

-- 索引
CREATE INDEX IF NOT EXISTS idx_mistakes_user_id    ON mistakes(user_id);
CREATE INDEX IF NOT EXISTS idx_mistakes_mastery    ON mistakes(user_id, mastery);
CREATE INDEX IF NOT EXISTS idx_mistakes_next_review ON mistakes(user_id, next_review_at);
CREATE INDEX IF NOT EXISTS idx_mistakes_subject    ON mistakes(user_id, subject);

-- 默认数据: admin 角色
INSERT OR IGNORE INTO roles (id, name, code, description) VALUES (1, '管理员', 'admin', '系统管理员');
INSERT OR IGNORE INTO roles (id, name, code, description) VALUES (2, '普通用户', 'user', '普通用户');

-- 默认权限
INSERT OR IGNORE INTO permissions (id, name, code, description) VALUES (1, '查看错题', 'mistake:read', '查看错题列表与详情');
INSERT OR IGNORE INTO permissions (id, name, code, description) VALUES (2, '管理错题', 'mistake:write', '新增/编辑/删除错题');
INSERT OR IGNORE INTO permissions (id, name, code, description) VALUES (3, '删除错题', 'mistake:delete', '删除错题');
INSERT OR IGNORE INTO permissions (id, name, code, description) VALUES (4, '复习', 'review', '使用复习功能');
INSERT OR IGNORE INTO permissions (id, name, code, description) VALUES (5, '系统管理', 'admin:all', '系统全部权限');

-- admin 角色拥有所有权限
INSERT OR IGNORE INTO role_permissions (role_id, permission_id) VALUES (1, 1);
INSERT OR IGNORE INTO role_permissions (role_id, permission_id) VALUES (1, 2);
INSERT OR IGNORE INTO role_permissions (role_id, permission_id) VALUES (1, 3);
INSERT OR IGNORE INTO role_permissions (role_id, permission_id) VALUES (1, 4);
INSERT OR IGNORE INTO role_permissions (role_id, permission_id) VALUES (1, 5);

-- user 角色拥有基本权限
INSERT OR IGNORE INTO role_permissions (role_id, permission_id) VALUES (2, 1);
INSERT OR IGNORE INTO role_permissions (role_id, permission_id) VALUES (2, 2);
INSERT OR IGNORE INTO role_permissions (role_id, permission_id) VALUES (2, 4);

-- 默认 admin 用户 (密码: 123456, BCrypt)
INSERT OR IGNORE INTO users (id, username, password, nickname, email) VALUES (1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5Eh', '管理员', 'admin@mistake.local');
-- 默认 admin 用户关联 admin 角色
INSERT OR IGNORE INTO user_roles (user_id, role_id) VALUES (1, 1);
