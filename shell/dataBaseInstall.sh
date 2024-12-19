# 設置變數
MYSQL_COMPOSE_FILE="docker-compose.prod-mysql.yml"

# 停止並刪除現有的 MySQL 容器（如果有）
echo "停止並移除現有的 MySQL 容器..."
sudo docker compose -f "$MYSQL_COMPOSE_FILE" down

# 啟動 MySQL 容器
echo "啟動 MySQL 容器..."
sudo docker compose -f "$MYSQL_COMPOSE_FILE" up -d || { echo "啟動 MySQL 容器失敗"; exit 1; }


