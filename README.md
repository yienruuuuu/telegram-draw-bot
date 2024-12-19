# Telegram-draw-bot

<p align="center"><img alt="Telegram-draw-bot-logo" height="300" src="pic/header.png"></p>
<p align="center">
  <img src="https://img.shields.io/github/license/yienruuuuu/telegram-draw-bot?style=for-the-badge" alt="">
  <img src="https://img.shields.io/github/stars/yienruuuuu/telegram-draw-bot?style=for-the-badge" alt="">
  <img src="https://img.shields.io/github/issues/yienruuuuu/telegram-draw-bot?style=for-the-badge" alt="">
  <img src="https://img.shields.io/github/forks/yienruuuuu/telegram-draw-bot?style=for-the-badge" alt="">
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Author-Yienruuuuu-cyan?style=flat-square" alt="">
  <img src="https://img.shields.io/badge/Open%20Source-Yes-cyan?style=flat-square" alt="">
  <img src="https://img.shields.io/badge/Written%20In-Java-cyan?style=flat-square" alt="">
</p>

**一個同時支援上傳、頻道管理、抽卡與收取Telegram stars的Telegram BOT服務**

## 需求
此專案使用以下技術
* JDK 17
* SpringBoot 3.1.5
* Gradle 8.6
* MySql 8.0.36
* TelegramBots 7.10.0
* Caffeine 3.1.8
* flyway 

## 運行環境
* 開發環境: Windows 10 intelliJ IDEA
* 運行環境: GCP Debian 6.1


## 功能

### 資源控管機器人
- [x] 上傳圖片/影片/GIF都會被做成"資源"
- [x] 管理者可以管理"資源"
- [x] 管理者可以管理卡池
- [x] 管理者可以管理卡牌

### 主抽卡機器人
- [x] 會員可在卡池抽卡，且可快速續抽
- [x] 根據TG預設初始語言，但可由會員自己切換語系
- [x] 可以從頻道取得積分碼，並增加積分
- [x] 根據會員語系支援多語系回復(新增語系的模板存在於resources/common-script內)
- [x] 會員狀態/積分查詢
- [x] 使會員產生邀請連結，被邀請者與邀請者同時增加積分
- [x] 遊玩色子遊戲以增加積分
- [x] 會員支付積分以購買影片下載/轉傳權限
- [x] 會員可支付telegram stars來購買積分

### 頻道管理機器人

- [x] 管理積分作弊碼的CRUD及發佈到頻道中



## 安裝
* 請先創建好三個BOT並取得token
    * 主BOT
    * 頻道管理BOT
    * 資源管理BOT
* 請先創建好兩個頻道
    * BOT內部溝通頻道(主BOT與資源控管BOT設為管理員)
    * 公開頻道(頻道控管BOT設為管理員) 

### 本地安裝
以下將會引導你如何安裝此專案到你的電腦上。
1. 安裝mysql 8
   1. 自官網文檔安裝mysql 8
   https://dev.mysql.com/downloads/mysql/
   2. 也可以透過運行docker-compose.dev-mysql.yml，起一個資料庫在docker容器內
2. 配置application-local.properties資料庫參數 
3. 初次運行專案(flyway會創建資料表及必要參數)
4. 停下服務，將創建好的主機器人、頻道控管機器人、資源控管機器人的token填入資料庫.bot.token內
5. 再次運行服務，可以看到啟動log出現各機器人"機器人 XXX 註冊完成"則代表連線成功
6. 透過發送訊息，取得兩個頻道的id
7. 停下服務，填寫application-local.properties的以下兩個參數
   1. bot.communicate-channel
   2. bot.public-channel
8. 啟動服務並繼續使用


## linux安裝

以下將會引導你如何安裝此專案到你的GCP VM上。
* 請先新建好GCP VM、設置白名單、並透過ssh工具連線成功
* 先安裝好docker

1. 運行/shell/pull.sh來從github拉下專案
2. 運行dataBaseInstall.sh來創建容器內資料庫
3. 運行build.sh來運行服務(flyway會創建資料表及必要參數)
4. 設置遠端連線 GCP 的 MySQL 資料庫(透過VCP網路>防火牆來設置)
   https://reurl.cc/DK4ZpR
5. 將創建好的主機器人、頻道控管機器人、資源控管機器人的token填入資料庫.bot.token內
6. 再次運行服務，可以看到啟動log出現各機器人"機器人 XXX 註冊完成"則代表連線成功
7. 透過發送訊息，取得兩個頻道的id
8. 停下服務，填寫application-prod.properties的以下兩個參數
    1. bot.communicate-channel
    2. bot.public-channel
9. 啟動服務並繼續使用




## 致謝

-   [Telegram Bot文檔](https://core.telegram.org/bots/api)
-   [Telegram Bot API -rubenlagus](https://github.com/rubenlagus/TelegramBots)


## Terms and Conditions

This project is intended for personal use and educational purposes only, and is not designed for activities that violate Telegram's Terms of Service or community guidelines.*

Use Responsibly: Please adhere to Telegram's official API usage policies when interacting with their platform.
Prohibited Uses: Do not use this library for spamming, unsolicited messages, botting, or other malicious activities.
No Malicious Intent: There will be no support provided for those who intend to use this library for illegal or harmful purposes.
Rate Limiting and Delays: Use reasonable (human-like) delays between API requests to avoid violating Telegram's rate-limiting policies.
Independence Disclaimer: This library is in no way affiliated with, authorized, maintained, sponsored, or endorsed by Telegram or any of its affiliates or subsidiaries. It is an independent, unofficial project and should be used at your own risk.
Risk and Responsibility
No Guarantees: Due to the nature of this project, features and functionalities may change or break as Telegram updates its platform.
Limited Liability: The contributors and maintainers of this library are not responsible for any misuse, improper usage, or consequences arising from the use of this project.
Compliance: Users are solely responsible for ensuring their use of this library complies with all applicable laws and regulations, as well as Telegram's Terms of Service.
Usage Acknowledgment
By using this library, you acknowledge and agree to these Terms and Conditions. The authors and contributors are not liable for any damages or issues arising from its use.
