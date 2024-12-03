SET @language_id_zh_hans = 3;
-- 递增 ID
SET @current_id = 59;

-- 新增語系
INSERT INTO language (id, language_code, language_name)
VALUES (@language_id_zh_hans, 'zh-hans', '簡體中文');

-- 新增start公告
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '
欢迎使用Dance chicks的抽卡机器人 🤖🥰
/start - 打印帮助信息
/invite - 邀请用户，获取积分
/pool - 查看卡池
/my_status - 查看你的账号信息
/get_point - 如何获得积分');

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (1, @current_id);

SET @current_id = @current_id + 1;

-- 新增公告類型 INVITE_MESSAGE_PREFIX
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '请将以下链接发送给还未使用过机器人的新用户:');
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (2, @current_id);
SET @current_id = @current_id + 1;

-- 新增公告類型 INVITE_MESSAGE_SUFFIX
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '用户通过你的邀请链接注册成功后，你和他将分别获得 100 积分');
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (3, @current_id);
SET @current_id = @current_id + 1;

-- 新增公告類型 NOT_REGISTERED
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '提醒：您尚未注册，请使用 /start 指令开始注册。');
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (4, @current_id);
SET @current_id = @current_id + 1;

-- 新增公告類型 USER_STATUS_QUERY
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '
👤玩家ID:{USER_ID}
👑玩家身份:{USER_ROLE}
🏧免费积分:{FREE_POINT}
🔱付费积分:{PAID_POINT}
📆注册时间:{REGISTER_TIME}');

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (5, @current_id);

SET @current_id = @current_id + 1;

-- 新增無卡池存在公告
INSERT INTO text (id, language_id, content)
VALUES (@current_id , @language_id_zh_hans, '🤡当前无卡池开放，敬请期待下次开放！🤡');

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (7, @current_id );

SET @current_id = @current_id + 1;

-- 抽卡消息
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '🔮✨抽卡✨🔮');

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (8, @current_id);

SET @current_id = @current_id + 1;

-- 積分獲取方式公告
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (9, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '
可以通过以下方式获得积分
- 【邀请用户】: 点击 /invite 获取邀请链接。
- 【限时活动】: 关注 @dance_chicks 频道，获得 100 积分，且频道不定时发布积分兑换码。
- 【玩游戏】: 发送一个 🎲，点击 /game 了解如何发送。
- 【👑购买付费积分👑】: 点击 /paid 以 Telegram Star⭐️ 购买。
');

SET @current_id = @current_id + 1;

-- 積分獲取方式公告
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (10,  @current_id);

INSERT INTO text (id, language_id, content)
VALUES ( @current_id, @language_id_zh_hans, '
您每天有一次掷骰子的机会。
点击上方的🎲，按照提示点击即可掷出🎲，你也可以手动掷出🎲。
如果你的客户端看不到🎲，请更新 Telegram 到最新版本.
');

SET @current_id = @current_id + 1;

-- 已玩過骰子公告
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (11, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '
你今天已经玩过骰子游戏了，请明天再来玩吧！
');

SET @current_id = @current_id + 1;

-- 使用條約
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (12, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '
以上为使用合约，请问是否已阅读且同意?
');

SET @current_id = @current_id + 1;

-- 積分不足公告
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (13, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '
您的积分不足 : {POINT}，请充值或购买积分。
可以通过以下方式获得积分
- 【邀请用户】: 点击 /invite 获取邀请链接。
- 【限时活动】: 关注 @dance_chicks 频道，获得 100 积分，且频道不定时发布积分兑换码。
- 【玩游戏】: 发送一个 🎲，点击 /game 了解如何发送。
- 【👑购买付费积分👑】: 点击 /paid 以 Telegram Star⭐️ 购买。
');

SET @current_id = @current_id + 1;

-- 取得下載權限按鈕
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (14, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '💎20 积分获取下载权限。');

SET @current_id = @current_id + 1;

-- 購買付費積分公告
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (15, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '
1 Telegram Star⭐️ = 1 付费积分
您要购买多少呢?
');

SET @current_id = @current_id + 1;

-- 抽卡消息
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '🎊再抽一次🎊');

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (17, @current_id);
