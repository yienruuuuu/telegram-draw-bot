INSERT INTO language (language_code, language_name)
VALUES ('zh-hant', '繁體中文'),
       ('en', 'English');

INSERT INTO text (language_id, content)
VALUES ('1', '歡迎使用');

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
/start - 打印帮助信息
/invite - 邀請用戶，獲取積分
/mystat - 查看你的帳號資訊
/getcredit - 如何獲得積分
'),
       (2, '
This is an announcement in English
');
-- 關聯繁體中文的公告文字
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (1, 2);
-- 關聯英文的公告文字
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (1, 3);

