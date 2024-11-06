DROP TABLE IF EXISTS bot;
CREATE TABLE bot
(
    bot_id      INT AUTO_INCREMENT PRIMARY KEY,
    bot_token   VARCHAR(512),
    description VARCHAR(512),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS language;
CREATE TABLE language
(
    id            INT PRIMARY KEY AUTO_INCREMENT, -- 表ID
    language_code VARCHAR(20) NOT NULL,           -- 語系代碼（例如：'zh-hant', 'en'）
    language_name VARCHAR(50) NOT NULL            -- 語系名稱（例如：'繁體中文', 'English')
);

DROP TABLE IF EXISTS user;
CREATE TABLE user
(
    id               INT PRIMARY KEY AUTO_INCREMENT,             -- 表ID
    role             VARCHAR(50)         NOT NULL,               -- 身分
    telegram_user_id VARCHAR(50)         NOT NULL,               -- telegram id
    first_name       varchar(50),                                -- 名字
    language_id      VARCHAR(20),                                -- 語系代碼（例如：'zh-hant', 'en'）
    is_block         boolean             NOT NULL DEFAULT FALSE, -- 是否為黑名單
    free_points      INT       DEFAULT 0 NOT NULL,               -- 免費積分欄位
    purchased_points INT       DEFAULT 0 NOT NULL,               -- 付費積分欄位
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS resource;
CREATE TABLE resource
(
    id                 INT PRIMARY KEY AUTO_INCREMENT,
    type               VARCHAR(50) NOT NULL,
    rarity_level       VARCHAR(50) NOT NULL DEFAULT 'NORMAL',
    file_id_main_bot   TEXT        NOT NULL,
    file_id_manage_bot TEXT        NOT NULL,
    created_at         TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP            DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS resource_text;
CREATE TABLE resource_text
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    resource_id INT NOT NULL, -- 外鍵，對應 resource 表
    text_id     INT NOT NULL  -- 外鍵，對應 text 表
);

DROP TABLE IF EXISTS collection_book;
CREATE TABLE collection_book
(
    id                 INT PRIMARY KEY AUTO_INCREMENT,
    type               VARCHAR(50) NOT NULL,
    rarity_level       VARCHAR(50) NOT NULL DEFAULT 'NORMAL',
    file_id_main_bot   TEXT        NOT NULL,
    file_id_manage_bot TEXT        NOT NULL,
    created_at         TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP            DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


DROP TABLE IF EXISTS card_pool;
CREATE TABLE card_pool
(
    id               INT PRIMARY KEY AUTO_INCREMENT,
    is_open          boolean NOT NULL DEFAULT FALSE,
    is_limit_edition boolean NOT NULL DEFAULT FALSE,
    created_at       TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
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
    language_id VARCHAR(50) NOT NULL,
    content     TEXT        NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS announcement;
CREATE TABLE announcement
(
    id         INT PRIMARY KEY AUTO_INCREMENT,
    text_id    VARCHAR(50) NOT NULL,
    sequence   INTEGER     NOT NULL DEFAULT 0, -- 排序編號
    created_at TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP            DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS tasks;
CREATE TABLE tasks
(
    id        INT PRIMARY KEY AUTO_INCREMENT,
    title     VARCHAR(50),
    completed BOOLEAN NOT NULL DEFAULT false
);



