DROP TABLE IF EXISTS bot;
CREATE TABLE bot
(
    id                     INT AUTO_INCREMENT PRIMARY KEY,
    type                   VARCHAR(50) NOT NULL DEFAULT 'MAIN',
    bot_token              VARCHAR(512),
    description            VARCHAR(512),
    bot_telegram_user_name VARCHAR(50), -- 機器人的telegram用戶名
    created_at             TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,
    updated_at             TIMESTAMP            DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS user;
CREATE TABLE user
(
    id                  INT PRIMARY KEY AUTO_INCREMENT,             -- 表ID
    role                VARCHAR(16)         NOT NULL,               -- 身分
    telegram_user_id    VARCHAR(16)         NOT NULL,               -- telegram id
    first_name          varchar(25),                                -- 名字
    language_id         INT,                                        -- 語系代碼（例如：'zh-hant', 'en'）
    is_block            boolean             NOT NULL DEFAULT FALSE, -- 是否為黑名單
    free_points         INT       DEFAULT 0 NOT NULL,               -- 免費積分欄位
    purchased_points    INT       DEFAULT 0 NOT NULL,               -- 付費積分欄位
    last_pick_rare_time TIMESTAMP,                                  -- 上次抽稀有卡時間
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,        -- 註冊時間
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS language;
CREATE TABLE language
(
    id            INT PRIMARY KEY AUTO_INCREMENT, -- 表ID
    language_code VARCHAR(20) NOT NULL,           -- 語系代碼（例如：'zh-hant', 'en'）
    language_name VARCHAR(20) NOT NULL            -- 語系名稱（例如：'繁體中文', 'English')
);

DROP TABLE IF EXISTS resource;
CREATE TABLE resource
(
    id                 INT PRIMARY KEY AUTO_INCREMENT,
    file_type          VARCHAR(16)  NOT NULL DEFAULT 'OTHER',
    rarity_type        VARCHAR(16)  NOT NULL DEFAULT 'NORMAL',
    file_id_main_bot   VARCHAR(128),          -- 主BOT的file_id
    file_id_manage_bot VARCHAR(128),          -- 資源控管Bot的file_id
    tags               VARCHAR(255),          -- 資源標籤
    unique_id          VARCHAR(52) NOT NULL, -- telegram資源的唯一識別碼
    created_at         TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY idx_unique_id (unique_id)      -- 唯一索引
);

DROP TABLE IF EXISTS resource_text;
CREATE TABLE resource_text
(
    resource_id INT NOT NULL, -- 外鍵，對應 resource 表
    text_id     INT NOT NULL, -- 外鍵，對應 text 表
    PRIMARY KEY (resource_id, text_id)
);

DROP TABLE IF EXISTS user_card;
CREATE TABLE user_card
(
    user_id    INT NOT NULL,
    card_id    INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


DROP TABLE IF EXISTS card_pool;
CREATE TABLE card_pool
(
    id               INT PRIMARY KEY AUTO_INCREMENT,
    start_at         TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    end_at           TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    is_open          boolean NOT NULL DEFAULT FALSE,
    is_limit_edition boolean NOT NULL DEFAULT FALSE,
    created_at       TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS card_pool_text;
CREATE TABLE card_pool_text
(
    card_pool_id INT NOT NULL, -- 卡池id
    text_id      INT NOT NULL, -- 文字ID
    PRIMARY KEY (card_pool_id, text_id)
);

DROP TABLE IF EXISTS card;
CREATE TABLE card
(
    id           INT PRIMARY KEY AUTO_INCREMENT,
    card_pool_id INT NOT NULL,               -- 卡池id
    total_amount INT,                        -- 當有限量，限量數量
    drop_rate    DECIMAL(5, 2) DEFAULT 1.00, -- 抽卡機率，以百分比表示但以權重計算
    resource_id  INT NOT NULL                -- 資源ID
);

DROP TABLE IF EXISTS text;
CREATE TABLE text
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    language_id INT  NOT NULL,
    content     TEXT NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS announcement;
CREATE TABLE announcement
(
    id         INT PRIMARY KEY AUTO_INCREMENT,
    type       VARCHAR(50) NOT NULL default 'OTHER',
    sequence   INTEGER     NOT NULL DEFAULT 0, -- 排序編號
    created_at TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP            DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS announcement_text;
CREATE TABLE announcement_text
(
    announcement_id INT NOT NULL, -- 公告id
    text_id         INT NOT NULL, -- 文字ID
    PRIMARY KEY (announcement_id, text_id)
);




