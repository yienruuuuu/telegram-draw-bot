SET @language_id_zh_hans = 3;
-- 遞增 ID
SET @current_id = 59;

-- 新增語系
INSERT INTO language (id, language_code, language_name)
VALUES (@language_id_zh_hans, 'zh-hans', '簡體中文');

-- 新增start公告
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '
歡迎使用Dance chicks的抽卡機器人 🤖🥰
/start - 打印帮助信息
/invite - 邀請用戶，獲取積分
/pool - 查看卡池
/my_status - 查看你的帳號資訊
/get_point - 如何獲得積分');

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (1, @current_id);

SET @current_id = @current_id + 1;


-- 新增公告類型 INVITE_MESSAGE_PREFIX
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '請將以下鏈接發送給還未使用過機器人的新用戶:');
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (2, @current_id);
SET @current_id = @current_id + 1;

-- 新增公告類型INVITE_MESSAGE_SUFFIX
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '用戶通過你的邀請鏈接註冊成功後，你和他將分別獲得 100 積分');
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (3, @current_id);
SET @current_id = @current_id + 1;

-- 新增公告類型 NOT_REGISTERED
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '提醒：您尚未註冊，請使用 /start 指令開始註冊。');
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (4, @current_id);
SET @current_id = @current_id + 1;


-- 新增公告類型 USER_STATUS_QUERY
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '
👤玩家ID:{USER_ID}
👑玩家身分:{USER_ROLE}
🏧免費積分:{FREE_POINT}
🔱付費積分:{PAID_POINT}
📆註冊時間:{REGISTER_TIME}');

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (5, @current_id);

SET @current_id = @current_id + 1;


-- 新增無卡池存在公告
INSERT INTO text (id, language_id, content)
VALUES (@current_id , @language_id_zh_hans, '🤡當前無卡池開放，敬請期待下次開放！🤡');

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (7, @current_id );

SET @current_id = @current_id + 1;


-- 抽卡訊息
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '🔮✨抽卡✨🔮');

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (8, @current_id);

SET @current_id = @current_id + 1;

-- 積分取得方式公告
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (9, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '
可以透過以下方式獲得積分
- 【邀請用戶】: 點選 /invite 獲取邀請鏈接。
- 【限時活動】: 關注 @dance_chicks 頻道，獲得 100 積分，且頻道不定時釋出積分兌換碼。
- 【玩遊戲】: 發送一個 🎲，點擊 /game 了解如何傳送。
- 【👑購買付費積分👑】: 點擊 /paid 以 Telegram Star⭐️ 購買。
');

SET @current_id = @current_id + 1;


-- 積分取得方式公告
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (10,  @current_id);

INSERT INTO text (id, language_id, content)
VALUES ( @current_id, @language_id_zh_hans, '
您每天有一次擲骰子的機會。
點擊上方的🎲，按照提示點擊即可擲出🎲，你也可以手動擲出🎲。
如果你的客戶端看不到🎲，請更新 Telegram 到最新版本.
');

SET @current_id = @current_id + 1;


-- 已玩過色子公告
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (11, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '
你今天已經玩過骰子遊戲了，請明天再來玩吧！
');

SET @current_id = @current_id + 1;


-- 使用條約
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (12, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '
以上為使用合約，請問是否已閱讀且同意?
');

SET @current_id = @current_id + 1;


-- 積分不足公告
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (13, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '
您的積分不足 : {POINT}，請充值或購買積分。
可以透過以下方式獲得積分
- 【邀請用戶】: 點選 /invite 獲取邀請鏈接。
- 【限時活動】: 關注 @dance_chicks 頻道，獲得 100 積分，且頻道不定時釋出積分兌換碼。
- 【玩遊戲】: 發送一個 🎲，點擊 /game 了解如何傳送。
- 【👑購買付費積分👑】: 點擊 /paid 以 Telegram Star⭐️ 購買。
');

SET @current_id = @current_id + 1;


-- 取得下載權限按鍵
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (14, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '💎20 積分取得下載權限。');

SET @current_id = @current_id + 1;

-- 購買付費積分公告
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (@current_id, 42);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '
1 Telegram Star⭐️ = 1 付費積分
您要購買多少呢?
');

SET @current_id = @current_id + 1;


-- 抽卡訊息
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '🎊再抽一次🎊');

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (17, @current_id);