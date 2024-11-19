INSERT INTO language (language_code, language_name)
VALUES ('zh-hant', '繁體中文'),
       ('en', 'English');

INSERT INTO bot (bot_token, description, type)
VALUES ('', '主BOT', 'MAIN'),
       ('', '資源控管BOT', 'FILE_MANAGE'),
       ('', '頻道BOT', 'CHANNEL'),
       ('', '交易BOT', 'TRADE');

-- 新增公告
INSERT INTO announcement (type, sequence)
VALUES ('START_MESSAGE', 1);

-- 繁體中文的公告說明
INSERT INTO text (language_id, content)
VALUES (1, '
歡迎使用Yuki的抽卡機器人 🤖🥰
/start - 打印帮助信息
/invite - 邀請用戶，獲取積分
/my_status - 查看你的帳號資訊
/getcredit - 如何獲得積分'),
       (2, '
Welcome to Yuki''s Gacha Bot 🤖🥰
/start - Display help information
/invite - Invite users to earn points
/my_status - Check your account information
/getcredit - Learn how to earn points');

-- 關聯繁體中文的公告文字
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (1, 1),
       (1, 2);


-- 新增公告類型 INVITE_MESSAGE_PREFIX 和 INVITE_MESSAGE_SUFFIX
INSERT INTO announcement (type, sequence)
VALUES ('INVITE_MESSAGE_PREFIX', 1),
       ('INVITE_MESSAGE_SUFFIX', 1);

-- 新增繁體中文和英文的 INVITE_MESSAGE_PREFIX 和 INVITE_MESSAGE_SUFFIX 說明
INSERT INTO text (language_id, content)
VALUES
    -- 繁體中文的 INVITE_MESSAGE_PREFIX
    (1, '請將以下鏈接發送給還未使用過機器人的新用戶:'),
    -- 英文的 INVITE_MESSAGE_PREFIX
    (2, 'Send the following link to new users who have not used this bot yet:'),

    -- 繁體中文的 INVITE_MESSAGE_SUFFIX
    (1, '用戶通過你的邀請鏈接註冊成功後，你和他將分別獲得 10 積分'),
    -- 英文的 INVITE_MESSAGE_SUFFIX
    (2, 'Once a user registers successfully through your invite link, both of you will receive 10 points.');

-- 關聯繁體中文和英文的 INVITE_MESSAGE_PREFIX 和 INVITE_MESSAGE_SUFFIX 文字
-- 假設這些新插入的文字的 ID 為 3、4、5、6
INSERT INTO announcement_text (announcement_id, text_id)
VALUES
    -- INVITE_MESSAGE_PREFIX 的公告文字（ID 2 對應此公告類型）
    (2, 3), -- 繁體中文
    (2, 4), -- 英文

    -- INVITE_MESSAGE_SUFFIX 的公告文字（ID 3 對應此公告類型）
    (3, 5), -- 繁體中文
    (3, 6);
-- 英文


-- 新增公告類型 NOT_REGISTERED
INSERT INTO announcement (type, sequence)
VALUES ('NOT_REGISTERED', 1);

-- 新增繁體中文和英文的 NOT_REGISTERED 說明
INSERT INTO text (language_id, content)
VALUES
    -- 繁體中文的 NOT_REGISTERED 提醒
    (1, '提醒：您尚未註冊，請使用 /start 指令開始註冊。'),
    -- 英文的 NOT_REGISTERED 提醒
    (2, 'Reminder: You are not registered yet. Please use the /start command to register.');

-- 關聯繁體中文和英文的 NOT_REGISTERED 文字
-- 假設這些新插入的文字的 ID 分別為 7 和 8
INSERT INTO announcement_text (announcement_id, text_id)
VALUES
    -- NOT_REGISTERED 的公告文字（ID 4 對應此公告類型）
    (4, 7), -- 繁體中文
    (4, 8);
-- 英文


-- 新增公告類型 NOT_REGISTERED
INSERT INTO announcement (type, sequence)
VALUES ('USER_STATUS_QUERY', 1);

-- 新增繁體中文和英文的 USER_STATUS_QUERY 說明
INSERT INTO text (language_id, content)
VALUES
    -- 繁體中文的 USER_STATUS_QUERY
    (1, '
👤玩家ID:{USER_ID}
👑玩家身分:{USER_ROLE}
🏧免費積分:{FREE_POINT}
🔱付費積分:{PAID_POINT}
📆註冊時間:{REGISTER_TIME}'),
    -- 英文的 USER_STATUS_QUERY
    (2, '
👤User ID: {USER_ID}
👑User Role: {USER_ROLE}
🏧Free Points: {FREE_POINT}
🔱Paid Points: {PAID_POINT}
📆Registration Date: {REGISTER_TIME}');

-- 關聯繁體中文和英文的 USER_STATUS_QUERY 文字
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (5, 9),
       (5, 10);


-- 新增公告
INSERT INTO announcement (type, sequence)
VALUES ('FILE_MANAGE_START_MESSAGE', 1);

-- 繁體中文的公告說明
INSERT INTO text (language_id, content)
    VALUE (1, '
歡迎使用資源操作機器人🤖
/start - 打印操作幫助
/edit_resource - 編輯資源
/list_resource_by_time_desc - 查看資源
/add_card_pool - 新增卡池
/list_card_pool_by_time_desc - 查看卡池');

-- 關聯繁體中文的公告文字
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (6, 11);


-- 新增無卡池存在公告
INSERT INTO announcement (type, sequence)
VALUES ('NO_POOL_OPEN_MESSAGE', 1);

-- 新增繁體中文和英文的 USER_STATUS_QUERY 說明
INSERT INTO text (language_id, content)
VALUES (1, '當前無卡池開放，敬請期待下次開放！'),
       (2, 'Currently, no card pools are open. Please stay tuned for the next opening!');

-- 關聯繁體中文和英文的 USER_STATUS_QUERY 文字
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (7, 12),
       (7, 13);



