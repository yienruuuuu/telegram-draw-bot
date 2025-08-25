ALTER TABLE `tg_draw_bot`.`resource`
    ADD COLUMN `send_text_type` varchar(64) NOT NULL DEFAULT 'TEXT' COMMENT '傳送文字方式類型' AFTER `is_in_used`;
