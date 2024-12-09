SET @language_id_zh_hans = (SELECT IFNULL(MAX(id), 0) + 1 FROM language);
SET @current_id = (SELECT IFNULL(MAX(id), 0) + 1 FROM text);

-- 新增語系
INSERT INTO language (id, language_code, language_name)
VALUES (@language_id_zh_hans, 'ar', '阿拉伯語');

-- 新增start公告
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '
مرحبًا بكم في روبوت السحب الخاص بـ Dance chicks 🤖🥰
/start - طباعة معلومات المساعدة
/invite - دعوة المستخدمين، واحصل على النقاط
/pool - عرض البطاقات المتاحة
/my_status - عرض معلومات حسابك
/get_point - كيفية الحصول على النقاط');

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (1, @current_id);

SET @current_id = @current_id + 1;

-- 新增公告類型 INVITE_MESSAGE_PREFIX
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, 'يرجى إرسال الرابط التالي إلى المستخدمين الذين لم يستخدموا الروبوت من قبل:');
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (2, @current_id);
SET @current_id = @current_id + 1;

-- 新增公告類型 INVITE_MESSAGE_SUFFIX
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, 'بعد تسجيل المستخدم عبر رابط الدعوة الخاص بك، ستحصل أنت وهو على 100 نقطة لكل منكما');
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (3, @current_id);
SET @current_id = @current_id + 1;

-- 新增公告類型 NOT_REGISTERED
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, 'تذكير: لم تقم بالتسجيل بعد، يرجى استخدام الأمر /start لبدء التسجيل.');
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (4, @current_id);
SET @current_id = @current_id + 1;

-- 新增公告類型 USER_STATUS_QUERY
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '
👤معرف اللاعب: {USER_ID}
👑دور اللاعب: {USER_ROLE}
🏧النقاط المجانية: {FREE_POINT}
🔱النقاط المدفوعة: {PAID_POINT}
📆وقت التسجيل: {REGISTER_TIME}');

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (5, @current_id);

SET @current_id = @current_id + 1;

-- 新增無卡池存在公告
INSERT INTO text (id, language_id, content)
VALUES (@current_id , @language_id_zh_hans, '🤡لا توجد بطاقات مفتوحة حاليًا، انتظر حتى يتم فتحها مرة أخرى!🤡');

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (7, @current_id );

SET @current_id = @current_id + 1;

-- 抽卡消息
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '🔮✨سحب البطاقات✨🔮');

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (8, @current_id);

SET @current_id = @current_id + 1;

-- 積分獲取方式公告
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (9, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '
يمكنك الحصول على النقاط عبر الطرق التالية:
- 【دعوة المستخدمين】: انقر /invite للحصول على رابط الدعوة.
- 【أنشطة محدودة الوقت】: تابع قناة @dance_chicks واحصل على 100 نقطة.
- 【لعب الألعاب】: أرسل 🎲، انقر /game لمعرفة كيفية اللعب.
- 【👑شراء النقاط المدفوعة👑】: انقر /paid لشراء النقاط عبر Telegram Star⭐️.
');

SET @current_id = @current_id + 1;

-- 骰子遊玩方式公告
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (10,  @current_id);

INSERT INTO text (id, language_id, content)
VALUES ( @current_id, @language_id_zh_hans, '
لديك فرصة واحدة لرمي 🎲 يوميًا.
اضغط على 🎲 أعلاه، واتبع الإرشادات للنقر لرمي 🎲، يمكنك أيضًا رمي 🎲 يدويًا.
إذا لم تتمكن من رؤية 🎲 في التطبيق الخاص بك، يرجى تحديث Telegram إلى أحدث إصدار.
');

SET @current_id = @current_id + 1;

-- 已玩過骰子公告
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (11, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '
لقد لعبت لعبة النرد اليوم. يرجى العودة غدًا للعب مرة أخرى!
');

SET @current_id = @current_id + 1;

-- 使用條約
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (12, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '
هذا هو العقد الخاص بالاستخدام، هل قرأته ووافقت عليه؟
');

SET @current_id = @current_id + 1;

-- 積分不足公告
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (13, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '
رصيد نقاطك غير كافٍ: {POINT}، يرجى الشحن أو شراء النقاط.
يمكنك الحصول على النقاط عبر الطرق التالية:
- 【دعوة المستخدمين】: انقر /invite للحصول على رابط الدعوة.
- 【أنشطة محدودة الوقت】: تابع قناة @dance_chicks واحصل على 100 نقطة.
- 【لعب الألعاب】: أرسل 🎲، انقر /game لمعرفة كيفية اللعب.
- 【👑شراء النقاط المدفوعة👑】: انقر /paid لشراء النقاط عبر Telegram Star⭐️.
');

SET @current_id = @current_id + 1;

-- 取得下載權限按鈕
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (14, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '💎20 نقطة للحصول على إذن التنزيل.');

SET @current_id = @current_id + 1;

-- 購買付費積分公告
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (15, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '
1 Telegram Star⭐️ = 1 نقطة مدفوعة
كم تريد أن تشتري؟
');

SET @current_id = @current_id + 1;

-- 抽卡消息
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '🎊اسحب مرة أخرى🎊');

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (17, @current_id);
