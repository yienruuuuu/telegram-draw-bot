ALTER TABLE `tg_draw_bot`.`card_pool`
    ADD COLUMN `pool_type` varchar(64) NOT NULL DEFAULT 'CARD' COMMENT '卡池類型' AFTER `resource_id`;
