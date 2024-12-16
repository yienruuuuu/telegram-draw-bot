SET @language_id_fa = (SELECT IFNULL(MAX(id), 0) + 1 FROM language);
SET @current_id = (SELECT IFNULL(MAX(id), 0) + 1 FROM text);

-- 新增語系：語系代碼 'fa'、語系名稱 '波斯語'、原語系名稱 'فارسی'、旗幟表情 '🇮🇷'
INSERT INTO language (id, language_code, language_name, original_language_name, flag_pattern)
VALUES (@language_id_fa, 'fa', '波斯語', 'فارسی','🇮🇷');

-- 新增 start 公告
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_fa, '
به ربات قرعه‌کشی کارت Dance chicks خوش آمدید 🤖🥰
/start - نمایش اطلاعات راهنما
/invite - دعوت از کاربران جدید و کسب امتیاز
/pool - نمایش کارت‌های موجود
/my_status - بررسی اطلاعات حساب شما
/get_point - نحوه کسب امتیاز را ببینید
');

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (1, @current_id);

SET @current_id = @current_id + 1;

-- 新增公告類型 INVITE_MESSAGE_PREFIX
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_fa, 'لینک زیر را برای کاربرانی که هنوز از این ربات استفاده نکرده‌اند ارسال کنید:');
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (2, @current_id);
SET @current_id = @current_id + 1;

-- 新增公告類型 INVITE_MESSAGE_SUFFIX
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_fa, 'وقتی کاربری از طریق لینک دعوت شما با موفقیت ثبت‌نام کرد، هم شما و هم او هر کدام ۱۰۰ امتیاز دریافت می‌کنید.');
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (3, @current_id);
SET @current_id = @current_id + 1;

-- 新增公告類型 NOT_REGISTERED
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_fa, 'یادآوری: شما هنوز ثبت‌نام نکرده‌اید. برای ثبت‌نام از دستور /start استفاده کنید.');
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (4, @current_id);
SET @current_id = @current_id + 1;

-- 新增公告類型 USER_STATUS_QUERY
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_fa, '
👤شناسه کاربر: {USER_ID}
👑نقش کاربر: {USER_ROLE}
🏧امتیاز رایگان: {FREE_POINT}
🔱امتیاز خریداری‌شده: {PAID_POINT}
📆تاریخ ثبت‌نام: {REGISTER_TIME}');
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (5, @current_id);

SET @current_id = @current_id + 1;

-- 新增無卡池存在公告
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_fa, '🤡در حال حاضر کارت فعالی وجود ندارد. منتظر مرحله بعدی باشید!🤡');
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (7, @current_id);

SET @current_id = @current_id + 1;

-- 抽卡消息
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_fa, '🔮✨قرعه‌کشی کارت✨🔮');
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (8, @current_id);

SET @current_id = @current_id + 1;

-- 積分獲取方式公告
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (9, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_fa, '
شما می‌توانید به روش‌های زیر امتیاز کسب کنید:
- 【دعوت از کاربران】: از دستور /invite استفاده کنید تا لینک دعوت خود را دریافت نمایید.
- 【رویدادهای محدود زمانی】: کانال @dance_chicks را دنبال کنید و 100 امتیاز بگیرید. این کانال گاهی کدهایی را برای دریافت امتیاز به اشتراک می‌گذارد.
- 【بازی کردن】: یک 🎲 ارسال کرده و دستور /game را وارد کنید تا نحوه بازی را یاد بگیرید.
- 【👑خرید امتیاز👑】: با دستور /paid و استفاده از Telegram Star⭐️، امتیاز خریداری کنید.
');

SET @current_id = @current_id + 1;

-- 骰子遊玩方式公告
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (10, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_fa, '
شما روزی یک بار می‌توانید 🎲 بیندازید.
با لمس کردن 🎲 یا ارسال دستی آن، تاس بیندازید.
اگر در برخی از کلاینت‌های تلگرام 🎲 نمایش داده نمی‌شود، تلگرام خود را به آخرین نسخه به‌روزرسانی کنید.
');

SET @current_id = @current_id + 1;

-- 已玩過骰子公告
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (11, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_fa, '
شما امروز بازی تاس را انجام داده‌اید. فردا دوباره تلاش کنید!
');

SET @current_id = @current_id + 1;

-- 使用條約
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (12, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_fa, '
شرایط استفاده در بالا ذکر شده است. آیا آن‌ها را خوانده و قبول دارید؟
');

SET @current_id = @current_id + 1;

-- 積分不足公告
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (13, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_fa, '
امتیاز شما کافی نیست: {POINT}. لطفاً شارژ کرده یا امتیاز خریداری کنید.
شما می‌توانید به روش‌های زیر امتیاز کسب کنید:
- 【دعوت از کاربران】: دستور /invite را وارد کنید تا لینک دعوت خود را دریافت نمایید.
- 【رویدادهای محدود زمانی】: کانال @dance_chicks را دنبال کنید و 100 امتیاز بگیرید. این کانال گاهی کدهایی را برای دریافت امتیاز به اشتراک می‌گذارد.
- 【بازی کردن】: یک 🎲 ارسال کرده و /game را وارد کنید تا نحوه بازی را یاد بگیرید.
- 【👑خرید امتیاز👑】: با دستور /paid و استفاده از Telegram Star⭐️، امتیاز خریداری کنید.
');

SET @current_id = @current_id + 1;

-- 取得下載權限按鈕
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (14, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_fa, '💎با پرداخت 20 امتیاز می‌توانید مجوز دانلود را دریافت کنید.');

SET @current_id = @current_id + 1;

-- 購買付費積分公告
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (15, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_fa, '
1 Telegram Star⭐️ = 1 امتیاز خریداری‌شده
چند امتیاز می‌خواهید بخرید؟
');

SET @current_id = @current_id + 1;

-- 抽卡消息 (第二種)
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_fa, '🎊دوباره کارت بکش🎊');

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (17, @current_id);
