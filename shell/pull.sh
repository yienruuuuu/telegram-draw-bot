#!/bin/bash

# 設置變數
GITHUB_REPO_URL="https://github.com/yienruuuuu/telegram-draw-bot.git"
PROJECT_DIR="./github"
MYSQL_COMPOSE_FILE="docker-compose.prod-mysql.yml"
WEB_COMPOSE_FILE="docker-compose.prod-app.yml"

# 檢查目錄是否已經存在且包含 .git 文件夾
if [ -d "$PROJECT_DIR/.git" ]; then
  echo "目錄 $PROJECT_DIR 已存在，更新代碼庫..."
  cd "$PROJECT_DIR" || exit
  git pull origin master || { echo "git pull 失敗"; exit 1; }
else
  echo "目錄 $PROJECT_DIR 不存在或不是 Git 倉庫，從 GitHub 克隆代碼庫..."
  rm -rf "$PROJECT_DIR"  # 刪除舊目錄（如果有的話）
  git clone "$GITHUB_REPO_URL" "$PROJECT_DIR" || { echo "git clone 失敗"; exit 1; }
  cd "$PROJECT_DIR" || exit
fi

# 將 docker-compose 文件複製到上一層
echo "複製 docker-compose 文件到上一層..."
if [ -f "$MYSQL_COMPOSE_FILE" ]; then
  cp -f "$MYSQL_COMPOSE_FILE" ../ || { echo "複製 $MYSQL_COMPOSE_FILE 失敗"; exit 1; }
else
  echo "$MYSQL_COMPOSE_FILE 不存在，跳過複製"
fi

if [ -f "$WEB_COMPOSE_FILE" ]; then
  cp -f "$WEB_COMPOSE_FILE" ../ || { echo "複製 $WEB_COMPOSE_FILE 失敗"; exit 1; }
else
  echo "$WEB_COMPOSE_FILE 不存在，跳過複製"
fi
