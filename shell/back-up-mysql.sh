#!/bin/bash

# 顯式加載 .env 檔案
source /home/airuikel69/tg_bot/.env

# 設定參數
CONTAINER_NAME="tg_bot_mysql"
BACKUP_DIR="/home/airuikel69/tg_bot/backup"
DATE=$(date +"%Y%m%d_%H%M%S")
BACKUP_FILE="${BACKUP_DIR}/mysql_backup_${DATE}.sql"

# 建立備份資料夾（如果不存在）
mkdir -p "$BACKUP_DIR"

# 執行備份
docker exec "$CONTAINER_NAME" mysqldump -u root -p${MYSQL_ROOT_PASSWORD} tg_draw_bot > "$BACKUP_FILE"

# 保留最近 7 天的備份
find "$BACKUP_DIR" -type f -mtime +7 -delete

# 確認是否成功
if [ $? -eq 0 ]; then
    echo "備份成功: $BACKUP_FILE"
else
    echo "備份失敗，請檢查日誌。"
fi

