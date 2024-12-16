-- 先假設我們改名變數為 @language_id_tr
SET @language_id_tr = (SELECT IFNULL(MAX(id), 0) + 1 FROM language);
SET @current_id = (SELECT IFNULL(MAX(id), 0) + 1 FROM text);

-- 新增語系：語系代碼 'tr'、語系名稱 '土耳其語'
INSERT INTO language (id, language_code, language_name,original_language_name,flag_pattern)
VALUES (@language_id_tr, 'tr', '土耳其語','Türkçe','🇹🇷');

-- 新增 start 公告
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_tr, '
Dance chicks kart çekme botuna hoş geldiniz 🤖🥰
/start - Yardım bilgilerini gösterir
/invite - Yeni kullanıcıları davet edin ve puan kazanın
/pool - Mevcut kartları görüntüle
/my_status - Hesap bilgilerinizi kontrol edin
/get_point - Puan nasıl kazanılır, öğrenin');

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (1, @current_id);

SET @current_id = @current_id + 1;

-- 新增公告類型 INVITE_MESSAGE_PREFIX
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_tr, 'Aşağıdaki linki henüz botu kullanmamış yeni kullanıcılara gönder:');
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (2, @current_id);
SET @current_id = @current_id + 1;

-- 新增公告類型 INVITE_MESSAGE_SUFFIX
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_tr, 'Bir kullanıcı senin davet linkinle başarıyla kayıt olduğunda, hem sen hem de o 100 puan kazanacaksınız.');
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (3, @current_id);
SET @current_id = @current_id + 1;

-- 新增公告類型 NOT_REGISTERED
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_tr, 'Hatırlatma: Henüz kayıtlı değilsin. Kayıt olmak için /start komutunu kullan.');
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (4, @current_id);
SET @current_id = @current_id + 1;

-- 新增公告類型 USER_STATUS_QUERY
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_tr, '
👤Kullanıcı ID: {USER_ID}
👑Kullanıcı Rolü: {USER_ROLE}
🏧Ücretsiz Puan: {FREE_POINT}
🔱Ücretli Puan: {PAID_POINT}
📆Kayıt Tarihi: {REGISTER_TIME}');
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (5, @current_id);

SET @current_id = @current_id + 1;

-- 新增無卡池存在公告
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_tr, '🤡Şu anda mevcut bir kart havuzu bulunmuyor. Bir sonraki açılışı bekleyin!🤡');
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (7, @current_id);

SET @current_id = @current_id + 1;

-- 抽卡消息
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_tr, '🔮✨Kart Çekme✨🔮');
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (8, @current_id);

SET @current_id = @current_id + 1;

-- 積分獲取方式公告
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (9, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_tr, '
Aşağıdaki yöntemlerle puan kazanabilirsiniz:
- 【Kullanıcı davet etmek】: /invite komutu ile davet linkini alın.
- 【Sınırlı süreli etkinlikler】: @dance_chicks kanalını takip ederek 100 puan kazanın. Kanalda zaman zaman puan kodları paylaşılacaktır.
- 【Oyun oynama】: Bir 🎲 gönderin ve /game yazarak nasıl oynanacağını öğrenin.
- 【👑Ücretli puan satın al👑】: /paid komutu ile Telegram Star⭐️ kullanarak puan satın alın.
');

SET @current_id = @current_id + 1;

-- 骰子遊玩方式公告
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (10, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_tr, '
Günde bir kez 🎲 atma hakkınız var.
Yukarıdaki 🎲 simgesine dokunarak veya elle göndererek zar atabilirsiniz.
Bazı Telegram istemcilerinde 🎲 görünmüyorsa, Telegram''ınızı en son sürüme güncelleyiniz.
');

SET @current_id = @current_id + 1;

-- 已玩過骰子公告
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (11, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_tr, '
Bugün zar oyununu zaten oynadınız. Yarın tekrar deneyebilirsiniz!
');

SET @current_id = @current_id + 1;

-- 使用條約
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (12, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_tr, '
Yukarıda kullanım koşulları yer almaktadır. Okudunuz ve kabul ediyor musunuz?
');

SET @current_id = @current_id + 1;

-- 積分不足公告
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (13, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_tr, '
Puanınız yetersiz: {POINT}. Lütfen yükleme yapın veya puan satın alın.
Aşağıdaki yöntemlerle puan kazanabilirsiniz:
- 【Kullanıcı davet etmek】: /invite komutu ile davet linkini alın.
- 【Sınırlı süreli etkinlikler】: @dance_chicks kanalını takip ederek 100 puan kazanın. Kanalda zaman zaman puan kodları paylaşılacaktır.
- 【Oyun oynama】: Bir 🎲 gönderin ve /game yazarak nasıl oynanacağını öğrenin.
- 【👑Ücretli puan satın al👑】: /paid komutu ile Telegram Star⭐️ kullanarak puan satın alın.
');

SET @current_id = @current_id + 1;

-- 取得下載權限按鈕
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (14, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_tr, '💎20 puan karşılığında indirme izni alabilirsiniz.');

SET @current_id = @current_id + 1;

-- 購買付費積分公告
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (15, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_tr, '
1 Telegram Star⭐️ = 1 ücretli puan
Kaç puan satın almak istiyorsunuz?
');

SET @current_id = @current_id + 1;

-- 抽卡消息(第二種)
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_tr, '🎊Tekrar kart çek🎊');

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (17, @current_id);
