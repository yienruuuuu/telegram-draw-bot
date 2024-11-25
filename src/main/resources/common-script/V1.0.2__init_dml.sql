INSERT INTO language (language_code, language_name)
VALUES ('zh-hant', '繁體中文'),
       ('en', 'English');

INSERT INTO bot (bot_token, description, type)
VALUES ('$BOT_TOKEN_MAIN', '主BOT', 'MAIN'),
       ('$BOT_TOKEN_FILE_MANAGE', '資源控管BOT', 'FILE_MANAGE'),
       ('$BOT_TOKEN_CHANNEL', '頻道管理BOT', 'CHANNEL');

-- 新增公告
INSERT INTO announcement (id, type, sequence)
VALUES (1, 'START_MESSAGE', 1);

-- 繁體中文的公告說明
INSERT INTO text (id, language_id, content)
VALUES (1, 1, '
歡迎使用Dance chicks的抽卡機器人 🤖🥰
/start - 打印帮助信息
/invite - 邀請用戶，獲取積分
/pool - 查看卡池
/my_status - 查看你的帳號資訊
/get_point - 如何獲得積分'),
       (2, 2, '
Welcome use Dance chicks Gacha Bot 🤖🥰
/start - Display help information
/invite - Invite users to earn points
/pool - Check the card pool
/my_status - Check your account information
/get_point - Learn how to earn points');

-- 關聯繁體中文的公告文字
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (1, 1),
       (1, 2);


-- 新增公告類型 INVITE_MESSAGE_PREFIX 和 INVITE_MESSAGE_SUFFIX
INSERT INTO announcement (id, type, sequence)
VALUES (2, 'INVITE_MESSAGE_PREFIX', 1),
       (3, 'INVITE_MESSAGE_SUFFIX', 1);

INSERT INTO text (id, language_id, content)
VALUES (3, 1, '請將以下鏈接發送給還未使用過機器人的新用戶:'),
       (4, 2, 'Send the following link to new users who have not used this bot yet:'),
       (5, 1, '用戶通過你的邀請鏈接註冊成功後，你和他將分別獲得 100 積分'),
       (6, 2, 'Once a user registers successfully through your invite link, both of you will receive 100 points.');

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (2, 3),
       (2, 4),
       (3, 5),
       (3, 6);


-- 新增公告類型 NOT_REGISTERED
INSERT INTO announcement (id, type, sequence)
VALUES (4, 'NOT_REGISTERED', 1);

INSERT INTO text (id, language_id, content)
VALUES (7, 1, '提醒：您尚未註冊，請使用 /start 指令開始註冊。'),
       (8, 2, 'Reminder: You are not registered yet. Please use the /start command to register.');

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (4, 7),
       (4, 8);


-- 新增公告類型 NOT_REGISTERED
INSERT INTO announcement (id, type, sequence)
VALUES (5, 'USER_STATUS_QUERY', 1);

-- 新增繁體中文和英文的 USER_STATUS_QUERY 說明
INSERT INTO text (id, language_id, content)
VALUES (9, 1, '
👤玩家ID:{USER_ID}
👑玩家身分:{USER_ROLE}
🏧免費積分:{FREE_POINT}
🔱付費積分:{PAID_POINT}
📆註冊時間:{REGISTER_TIME}'),
       (10, 2, '
👤User ID: {USER_ID}
👑User Role: {USER_ROLE}
🏧Free Points: {FREE_POINT}
🔱Paid Points: {PAID_POINT}
📆Registration Date: {REGISTER_TIME}');

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (5, 9),
       (5, 10);


-- 新增公告
INSERT INTO announcement (id, type, sequence)
VALUES (6, 'FILE_MANAGE_START_MESSAGE', 1);

INSERT INTO text (id, language_id, content)
    VALUE (11, 1, '
歡迎使用資源操作機器人🤖
/list_resource - 查看資源
/add_card_pool - 新增卡池
/list_card_pool - 查看卡池');

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (6, 11);


-- 新增無卡池存在公告
INSERT INTO announcement (id, type, sequence)
VALUES (7, 'NO_POOL_OPEN_MESSAGE', 1);

INSERT INTO text (id, language_id, content)
VALUES (12, 1, '🤡當前無卡池開放，敬請期待下次開放！🤡'),
       (13, 2, '🤡Currently, no card pools are open. Please stay tuned for the next opening!🤡');

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (7, 12),
       (7, 13);

-- 抽卡訊息
INSERT INTO announcement (id, type, sequence)
VALUES (8, 'PICK_CARD', 1);

INSERT INTO text (id, language_id, content)
VALUES (14, 1, '🔮✨抽卡✨🔮'),
       (15, 2, '🔮✨SUMMON✨🔮');

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (8, 14),
       (8, 15);


-- 積分取得方式公告
INSERT INTO announcement (id, type, sequence)
VALUES (9, 'GET_POINT_ANNOUNCEMENT', 1);

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (9, 30),
       (9, 31);

INSERT INTO text (id, language_id, content)
VALUES (30, 1, '
可以透過以下方式獲得積分
- 【邀請用戶】: 點選 /invite 獲取邀請鏈接。
- 【限時活動】: 關注 @dance_chicks 頻道，獲得 100 積分，且頻道不定時釋出積分兌換碼。
- 【玩遊戲】: 發送一個 🎲，點擊 /game 了解如何傳送。
- 【👑購買付費積分👑】: 點擊 /paid 以 Telegram Star⭐️ 購買。
'),
       (31, 2, '
You can earn free points through the following ways
- 【Invite Users】: Click /invite to get your invitation link.
- 【Limited-Time Events】: Follow @dance_chicks channel for 100 points, and channel will release points activities.
- 【Play Games】: Send a 🎲 or click /game to learn how to participate.
- 【👑Purchase Paid Points👑】: click /paid to use Telegram Star⭐️ to purchase paid points.
');


-- 積分取得方式公告
INSERT INTO announcement (id, type, sequence)
VALUES (10, 'GAME_DESCRIPTION', 1);

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (10, 32),
       (10, 33);

INSERT INTO text (id, language_id, content)
VALUES (32, 1, '
您每天有一次擲骰子的機會。
點擊上方的🎲，按照提示點擊即可擲出🎲，你也可以手動擲出🎲。
如果你的客戶端看不到🎲，請更新 Telegram 到最新版本.
'),
       (33, 2, '
You have one dice roll opportunity per day.
Click the 🎲 above and follow the instructions to roll the dice,
or you can manually roll the dice.
If you cannot see the 🎲 on your client,
please update Telegram to the latest version.
');


-- 已玩過色子公告
INSERT INTO announcement (id, type, sequence)
VALUES (11, 'HAS_PLAY_DICE_TODAY_MESSAGE', 1);

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (11, 34),
       (11, 35);

INSERT INTO text (id, language_id, content)
VALUES (34, 1, '
你今天已經玩過骰子遊戲了，請明天再來玩吧！
'),
       (35, 2, '
You have already played the dice game today, please come back tomorrow!
');

-- 使用條約
INSERT INTO announcement (id, type, sequence)
VALUES (12, 'TERM_MESSAGE', 1);

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (12, 36),
       (12, 37);

INSERT INTO text (id, language_id, content)
VALUES (36, 1, '
以上為使用合約，請問是否已閱讀且同意?
'),
       (37, 2, '
The above is the user agreement. Have you read and agreed to it?
');


-- 積分不足公告
INSERT INTO announcement (id, type, sequence)
VALUES (13, 'POINT_NOT_ENOUGH_MESSAGE', 1);

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (13, 38),
       (13, 39);

INSERT INTO text (id, language_id, content)
VALUES (38, 1, '
您的積分不足 : {POINT}，請充值或購買積分。
可以透過以下方式獲得積分
- 【邀請用戶】: 點選 /invite 獲取邀請鏈接。
- 【限時活動】: 關注 @dance_chicks 頻道，獲得 100 積分，且頻道不定時釋出積分兌換碼。
- 【玩遊戲】: 發送一個 🎲，點擊 /game 了解如何傳送。
- 【👑購買付費積分👑】: 點擊 /paid 以 Telegram Star⭐️ 購買。
'),
       (39, 2, '
Your points are insufficient: {"POINT"}. Please recharge or purchase points.
You can earn free points through the following ways
- 【Invite Users】: Click /invite to get your invitation link.
- 【Limited-Time Events】: Follow @dance_chicks channel for 100 points, and channel will release points activities.
- 【Play Games】: Send a 🎲 or click /game to learn how to participate.
- 【👑Purchase Paid Points👑】: click /paid to use Telegram Star⭐️ to purchase paid points.
');

-- 取得下載權限按鍵
INSERT INTO announcement (id, type, sequence)
VALUES (14, 'GET_DOWNLOAD_PERMISSION_BUTTON', 1);

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (14, 40),
       (14, 41);

INSERT INTO text (id, language_id, content)
VALUES (40, 1, '💎20 積分取得下載權限。'),
       (41, 2, '💎20 point for Download');


-- 購買付費積分公告
INSERT INTO announcement (id, type, sequence)
VALUES (15, 'PAYMENT_ANNOUNCEMENT', 1);

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (15, 42),
       (15, 43);

INSERT INTO text (id, language_id, content)
VALUES (42, 1, '
1 Telegram Star⭐️ = 1 付費積分
您要購買多少呢?
'),
       (43, 2, '
1 Telegram Star⭐️ = 1 Paid Points
How many do you want to purchase?
');

-- 作弊碼公告
INSERT INTO announcement (id, type, sequence)
VALUES (16, 'CHEAT_CODE_ANNOUNCEMENT', 1);

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (16, 44);

INSERT INTO text (id, language_id, content)
VALUES (44, 1, '
🔞作弊碼來囉~
直接對機器人輸入以下作弊碼即可獲得 {POINT_AMOUNT} 積分。
📆有效期間:
{VALIDITY_PERIOD}

🔞cheat code is here~
enter the following cheat code to the bot and get {POINT_AMOUNT} points instantly.
📆validity period:
{VALIDITY_PERIOD}
');

