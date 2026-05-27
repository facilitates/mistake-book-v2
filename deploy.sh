#!/bin/bash
# ============================================================
# 错题本 2.0 — 部署脚本
# 用法: ./deploy.sh [dev|prod]
# ============================================================
set -e

ENV=${1:-dev}
PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
JAR_FILE="target/mistake2.0-2.0.0-SNAPSHOT.jar"

echo ">>> 部署环境: ${ENV}"

# 1. 构建
echo ">>> 1/4 编译打包..."
cd "${PROJECT_DIR}"
mvn clean package -DskipTests -P"${ENV}" -q

# 2. 停止旧进程
echo ">>> 2/4 停止旧服务..."
if systemctl is-active --quiet mistake2.0 2>/dev/null; then
    sudo systemctl stop mistake2.0
fi

# 3. 部署
echo ">>> 3/4 部署..."
sudo mkdir -p /opt/mistake2.0/logs
sudo cp "${JAR_FILE}" /opt/mistake2.0/app.jar

# 4. 启动
echo ">>> 4/4 启动服务..."
export SPRING_PROFILES_ACTIVE="${ENV}"
sudo systemctl start mistake2.0

echo ">>> 部署完成! http://localhost:8080/api/v2/doc.html"
