#!/bin/bash

#!/bin/bash

# 定義變數
CURRENT_DIR=$(pwd)
PROJECT_DIR="./"
BUILD_JAR="$PROJECT_DIR/github/build/libs/tg_bot.jar"
COMPOSE_JAR="$PROJECT_DIR/tg_bot.jar"
DOCKER_COMPOSE_FILE="$PROJECT_DIR/docker-compose.prod-app.yml"

# Docker 容器名稱
DOCKER_CONTAINER_NAME="gradle_build_tg_bot"

# 移除已存在的同名容器
if docker ps -a --format '{{.Names}}' | grep -q "^${DOCKER_CONTAINER_NAME}$"; then
    echo "Removing existing container: $DOCKER_CONTAINER_NAME"
    docker rm -f "$DOCKER_CONTAINER_NAME"
fi

# 使用 Docker 中的 Gradle 來打包
echo "Starting Docker container to build the project with Gradle..."
docker run -d --name "$DOCKER_CONTAINER_NAME" -v "$CURRENT_DIR/github:/app" -w '/app' gradle:7.5 bash -c 'sleep 8000'

# 在容器內執行 Gradle build
echo "Running Gradle build inside the Docker container..."
docker exec -it "$DOCKER_CONTAINER_NAME" gradle bootJar

# 檢查 jar 檔案是否成功產生
if [ -f "$BUILD_JAR" ]; then
    echo "Build successful. Moving JAR file to Docker Compose directory..."
    # 將打包後的 jar 檔案移動到 Docker Compose 層級
    sudo mv "$BUILD_JAR" "$COMPOSE_JAR"
else
    echo "Build failed or JAR file not found at $BUILD_JAR"
    exit 1
fi

# 停止並移除 Docker 容器
echo "Stopping and removing the Docker container..."
docker stop "$DOCKER_CONTAINER_NAME"
docker rm "$DOCKER_CONTAINER_NAME"

# 2. 使用 Docker Compose 啟動服務
echo "Starting services with Docker Compose..."
docker compose -f "$DOCKER_COMPOSE_FILE" down
docker compose -f "$DOCKER_COMPOSE_FILE" up -d --build || { echo "Docker Compose failed to start services"; exit 1; }

# 3. 檢查容器狀態
echo "Checking running containers..."
docker ps | grep tg_bot_app && echo "tg_bot_app is running" || echo "tg_bot_app is not running"

# 檢查是否需要執行 down.sh 腳本
if [ -z "$1" ]; then
    echo "No arguments provided. Executing down.sh to stop other services..."
    "$CURRENT_DIR/down.sh"
fi

echo "Script execution completed."

