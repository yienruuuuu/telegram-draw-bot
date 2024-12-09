SET @language_id_zh_hans = (SELECT IFNULL(MAX(id), 0) + 1 FROM language);
SET @current_id = (SELECT IFNULL(MAX(id), 0) + 1 FROM text);

-- 新增語系
INSERT INTO language (id, language_code, language_name)
VALUES (@language_id_zh_hans, 'de', '德語');

-- 新增start公告
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '
Willkommen beim Dance chicks Zieh-Bot 🤖🥰
/start - Hilfeinformationen anzeigen
/invite - Benutzer einladen und Punkte sammeln
/pool - Kartenpool anzeigen
/my_status - Ihre Kontoinformationen anzeigen
/get_point - Wie man Punkte sammelt');

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (1, @current_id);

SET @current_id = @current_id + 1;

-- 新增公告類型 INVITE_MESSAGE_PREFIX
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, 'Bitte senden Sie den folgenden Link an neue Benutzer, die den Bot noch nicht verwendet haben:');
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (2, @current_id);
SET @current_id = @current_id + 1;

-- 新增公告類型 INVITE_MESSAGE_SUFFIX
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, 'Wenn ein Benutzer sich erfolgreich über Ihren Einladungslink registriert, erhalten Sie und er jeweils 100 Punkte.');
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (3, @current_id);
SET @current_id = @current_id + 1;

-- 新增公告類型 NOT_REGISTERED
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, 'Erinnerung: Sie haben sich noch nicht registriert. Bitte verwenden Sie den Befehl /start, um sich zu registrieren.');
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (4, @current_id);
SET @current_id = @current_id + 1;

-- 新增公告類型 USER_STATUS_QUERY
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '
👤Spieler-ID: {USER_ID}
👑Spielerrolle: {USER_ROLE}
🏧Kostenlose Punkte: {FREE_POINT}
🔱Bezahlte Punkte: {PAID_POINT}
📆Registrierungszeit: {REGISTER_TIME}');

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (5, @current_id);

SET @current_id = @current_id + 1;

-- 新增無卡池存在公告
INSERT INTO text (id, language_id, content)
VALUES (@current_id , @language_id_zh_hans, '🤡Derzeit ist kein Kartenpool geöffnet. Bitte warten Sie auf die nächste Öffnung!🤡');

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (7, @current_id );

SET @current_id = @current_id + 1;

-- 抽卡消息
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '🔮✨Karten ziehen✨🔮');

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (8, @current_id);

SET @current_id = @current_id + 1;

-- 積分獲取方式公告
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (9, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '
Sie können Punkte auf folgende Weise sammeln:
- 【Benutzer einladen】: Klicken Sie auf /invite, um Ihren Einladungslink zu erhalten.
- 【Zeitlich begrenzte Aktionen】: Folgen Sie dem Kanal @dance_chicks und erhalten Sie 100 Punkte. Der Kanal veröffentlicht gelegentlich Einlösecodes.
- 【Spiele spielen】: Senden Sie einen 🎲 und klicken Sie auf /game, um weitere Informationen zu erhalten.
- 【👑Bezahlte Punkte kaufen👑】: Klicken Sie auf /paid, um Punkte mit Telegram Star⭐️ zu kaufen.
');

SET @current_id = @current_id + 1;

-- 骰子遊玩方式公告
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (10,  @current_id);

INSERT INTO text (id, language_id, content)
VALUES ( @current_id, @language_id_zh_hans, '
Sie haben jeden Tag eine Chance, 🎲 zu würfeln.
Klicken Sie auf den 🎲 oben und folgen Sie den Anweisungen, um zu würfeln. Sie können auch manuell 🎲 würfeln.
Wenn Ihr Client 🎲 nicht anzeigen kann, aktualisieren Sie Telegram bitte auf die neueste Version.
');

SET @current_id = @current_id + 1;

-- 已玩過骰子公告
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (11, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '
Sie haben das Würfelspiel heute bereits gespielt. Bitte kommen Sie morgen wieder!
');

SET @current_id = @current_id + 1;

-- 使用條約
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (12, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '
Oben stehen die Nutzungsbedingungen. Haben Sie sie gelesen und zugestimmt?
');

SET @current_id = @current_id + 1;

-- 積分不足公告
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (13, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '
Ihre Punkte reichen nicht aus: {POINT}. Bitte laden Sie Ihr Konto auf oder kaufen Sie Punkte.
Sie können Punkte auf folgende Weise sammeln:
- 【Benutzer einladen】: Klicken Sie auf /invite, um Ihren Einladungslink zu erhalten.
- 【Zeitlich begrenzte Aktionen】: Folgen Sie dem Kanal @dance_chicks und erhalten Sie 100 Punkte. Der Kanal veröffentlicht gelegentlich Einlösecodes.
- 【Spiele spielen】: Senden Sie einen 🎲 und klicken Sie auf /game, um weitere Informationen zu erhalten.
- 【👑Bezahlte Punkte kaufen👑】: Klicken Sie auf /paid, um Punkte mit Telegram Star⭐️ zu kaufen.
');

SET @current_id = @current_id + 1;

-- 取得下載權限按鈕
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (14, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '💎20 Punkte, um die Download-Berechtigung zu erhalten.');

SET @current_id = @current_id + 1;

-- 購買付費積分公告
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (15, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '
1 Telegram Star⭐️ = 1 bezahlter Punkt
Wie viele möchten Sie kaufen?
');

SET @current_id = @current_id + 1;

-- 抽卡消息
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '🎊Noch einmal ziehen🎊');

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (17, @current_id);
