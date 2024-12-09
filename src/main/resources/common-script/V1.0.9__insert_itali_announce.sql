SET @language_id_zh_hans = (SELECT IFNULL(MAX(id), 0) + 1 FROM language);
SET @current_id = (SELECT IFNULL(MAX(id), 0) + 1 FROM text);

-- 新增語系
INSERT INTO language (id, language_code, language_name)
VALUES (@language_id_zh_hans, 'it', '義大利語');

-- 新增start公告
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '
Benvenuto nel bot di estrazione di carte di Dance chicks 🤖🥰
/start - Mostra le informazioni di aiuto
/invite - Invita utenti e guadagna punti
/pool - Visualizza le carte disponibili
/my_status - Controlla le informazioni del tuo account
/get_point - Come ottenere punti');

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (1, @current_id);

SET @current_id = @current_id + 1;

-- 新增公告類型 INVITE_MESSAGE_PREFIX
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, 'Invia il seguente link ai nuovi utenti che non hanno ancora utilizzato il bot:');
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (2, @current_id);
SET @current_id = @current_id + 1;

-- 新增公告類型 INVITE_MESSAGE_SUFFIX
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, 'Quando un utente si registra con successo tramite il tuo link di invito, tu e lui riceverete ciascuno 100 punti.');
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (3, @current_id);
SET @current_id = @current_id + 1;

-- 新增公告類型 NOT_REGISTERED
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, 'Promemoria: non sei ancora registrato. Usa il comando /start per iniziare la registrazione.');
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (4, @current_id);
SET @current_id = @current_id + 1;

-- 新增公告類型 USER_STATUS_QUERY
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '
👤ID giocatore: {USER_ID}
👑Ruolo giocatore: {USER_ROLE}
🏧Punti gratuiti: {FREE_POINT}
🔱Punti a pagamento: {PAID_POINT}
📆Data di registrazione: {REGISTER_TIME}');

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (5, @current_id);

SET @current_id = @current_id + 1;

-- 新增無卡池存在公告
INSERT INTO text (id, language_id, content)
VALUES (@current_id , @language_id_zh_hans, '🤡Al momento non ci sono carte disponibili. Aspetta la prossima apertura!🤡');

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (7, @current_id );

SET @current_id = @current_id + 1;

-- 抽卡消息
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '🔮✨Estrazione delle carte✨🔮');

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (8, @current_id);

SET @current_id = @current_id + 1;

-- 積分獲取方式公告
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (9, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '
Puoi ottenere punti nei seguenti modi:
- 【Invitare utenti】: Clicca su /invite per ottenere il tuo link di invito.
- 【Eventi a tempo limitato】: Segui il canale @dance_chicks e guadagna 100 punti. Il canale pubblica occasionalmente codici per riscattare punti.
- 【Giocare】: Invia un 🎲 e clicca su /game per scoprire come giocare.
- 【👑Acquista punti a pagamento👑】: Clicca su /paid per acquistare punti con Telegram Star⭐️.
');

SET @current_id = @current_id + 1;

-- 骰子遊玩方式公告
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (10,  @current_id);

INSERT INTO text (id, language_id, content)
VALUES ( @current_id, @language_id_zh_hans, '
Hai una possibilità al giorno di lanciare 🎲.
Clicca sul 🎲 sopra e segui le istruzioni per lanciarlo, oppure puoi lanciarlo manualmente.
Se non riesci a vedere il 🎲 nel tuo client, aggiorna Telegram all''ultima versione.
');

SET @current_id = @current_id + 1;

-- 已玩過骰子公告
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (11, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '
Hai già giocato al gioco dei dadi oggi. Torna domani per giocare di nuovo!
');

SET @current_id = @current_id + 1;

-- 使用條約
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (12, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '
Sopra ci sono i termini di utilizzo. Li hai letti e accettati?
');

SET @current_id = @current_id + 1;

-- 積分不足公告
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (13, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '
I tuoi punti non sono sufficienti: {POINT}. Effettua una ricarica o acquista punti.
Puoi ottenere punti nei seguenti modi:
- 【Invitare utenti】: Clicca su /invite per ottenere il tuo link di invito.
- 【Eventi a tempo limitato】: Segui il canale @dance_chicks e guadagna 100 punti. Il canale pubblica occasionalmente codici per riscattare punti.
- 【Giocare】: Invia un 🎲 e clicca su /game per scoprire come giocare.
- 【👑Acquista punti a pagamento👑】: Clicca su /paid per acquistare punti con Telegram Star⭐️.
');

SET @current_id = @current_id + 1;

-- 取得下載權限按鈕
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (14, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '💎20 punti per ottenere il permesso di scaricare.');

SET @current_id = @current_id + 1;

-- 購買付費積分公告
INSERT INTO announcement_text (announcement_id, text_id)
VALUES (15, @current_id);

INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '
1 Telegram Star⭐️ = 1 punto a pagamento
Quanti vuoi acquistare?
');

SET @current_id = @current_id + 1;

-- 抽卡消息
INSERT INTO text (id, language_id, content)
VALUES (@current_id, @language_id_zh_hans, '🎊Estrai di nuovo🎊');

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (17, @current_id);
