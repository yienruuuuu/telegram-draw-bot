-- 抽卡訊息
INSERT INTO announcement (id, type, sequence)
VALUES (17, 'PICK_AGAIN', 1);

INSERT INTO text (id, language_id, content)
VALUES (55, 1, '🎊再抽一次🎊'),
       (56, 2, '🎊SUMMON AGAIN🎊');

INSERT INTO announcement_text (announcement_id, text_id)
VALUES (17, 55),
       (17, 56);



