INSERT INTO `tg_draw_bot`.`user` (`id`, `role`, `telegram_user_id`, `first_name`, `language_id`, `is_block`, `free_points`, `purchased_points`, `created_at`, `updated_at`) VALUES (1, 'MANAGER', '1513052214', 'Ru', 1, 0, 0, 0,  '2024-11-14 06:58:32', '2024-11-14 06:58:48');

INSERT INTO `tg_draw_bot`.`resource` (`id`, `file_type`, `rarity_type`, `file_id_main_bot`, `file_id_manage_bot`, `tags`, `unique_id`, `has_been_card_before`, `created_at`, `updated_at`) VALUES (1, 'GIF', 'UR', 'CgACAgQAAyEFAASR9m7DAAMaZzRcuFSFtheMmxliaiFEjthUbvYAAgkGAAI1qBVQW7RJpzrunj02BA', 'CgACAgQAAxkBAAM7ZzVTWfG-oyjpNmuUs7Vq8ao9AfwAAgkGAAI1qBVQJjSaOq7gRs42BA', NULL, 'AgADCQYAAjWoFVA', 0, '2024-11-14 07:18:15', '2024-11-14 07:19:28');
INSERT INTO `tg_draw_bot`.`resource` (`id`, `file_type`, `rarity_type`, `file_id_main_bot`, `file_id_manage_bot`, `tags`, `unique_id`, `has_been_card_before`, `created_at`, `updated_at`) VALUES (2, 'PHOTO', 'SSR', 'AgACAgUAAyEFAASR9m7DAAMiZzWkjkacaZFp91EXkfzeWCDMwBUAAp3GMRv4U7FVFCxlUvTJ8hkBAAMCAAN5AAM2BA', 'AgACAgUAAxkBAAOMZzWkjiH6LNKFsJfvnFiBHmJ7gvYAAp3GMRv4U7FVah5fZzsFKwkBAAMCAAN5AAM2BA', NULL, 'AQADncYxG_hTsVV-', 0, '2024-11-14 07:19:42', '2024-11-14 07:21:14');
INSERT INTO `tg_draw_bot`.`resource` (`id`, `file_type`, `rarity_type`, `file_id_main_bot`, `file_id_manage_bot`, `tags`, `unique_id`, `has_been_card_before`, `created_at`, `updated_at`) VALUES (3, 'VIDEO', 'UR', 'BAACAgUAAyEFAASR9m7DAAMjZzWlBbJ3r7JDBft23QLYUpRlnngAAmwRAAL4U7FVRxWipfgzwY02BA', 'BAACAgUAAxkBAAOPZzWlBKGTow4hQG9bNAABkTIBUntOAAJsEQAC-FOxVYsINEn1DJi6NgQ', NULL, 'AgADbBEAAvhTsVU', 0, '2024-11-14 07:21:41', '2024-11-14 07:22:00');
INSERT INTO `tg_draw_bot`.`resource` (`id`, `file_type`, `rarity_type`, `file_id_main_bot`, `file_id_manage_bot`, `tags`, `unique_id`, `has_been_card_before`, `created_at`, `updated_at`) VALUES (4, 'GIF', 'SR', 'CgACAgUAAyEFAASR9m7DAAMZZzRaMIJDF4Mjx21WdkekAVnX8HYAAh4QAAK10ThWmduZjfPF0ek2BA', 'CgACAgUAAxkBAAOSZzWlau_qf11LedAWzI1QJRiROVkAAh4QAAK10ThWYUCfkw-2XxU2BA', NULL, 'AgADHhAAArXROFY', 0, '2024-11-14 07:23:23', '2024-11-14 07:23:24');

INSERT INTO `tg_draw_bot`.`text` (`id`, `language_id`, `content`, `created_at`, `updated_at`) VALUES (16, 1, 'ㄓㄓ', '2024-11-14 07:19:27', '2024-11-14 07:19:27');
INSERT INTO `tg_draw_bot`.`text` (`id`, `language_id`, `content`, `created_at`, `updated_at`) VALUES (17, 2, 'fhg', '2024-11-14 07:19:27', '2024-11-14 07:19:27');
INSERT INTO `tg_draw_bot`.`text` (`id`, `language_id`, `content`, `created_at`, `updated_at`) VALUES (18, 1, '好啊', '2024-11-14 07:21:13', '2024-11-14 07:21:13');
INSERT INTO `tg_draw_bot`.`text` (`id`, `language_id`, `content`, `created_at`, `updated_at`) VALUES (19, 2, '不要', '2024-11-14 07:21:13', '2024-11-14 07:21:13');
INSERT INTO `tg_draw_bot`.`text` (`id`, `language_id`, `content`, `created_at`, `updated_at`) VALUES (20, 1, '讚', '2024-11-14 07:21:59', '2024-11-14 07:21:59');
INSERT INTO `tg_draw_bot`.`text` (`id`, `language_id`, `content`, `created_at`, `updated_at`) VALUES (21, 2, '讚讚', '2024-11-14 07:21:59', '2024-11-14 07:21:59');
INSERT INTO `tg_draw_bot`.`text` (`id`, `language_id`, `content`, `created_at`, `updated_at`) VALUES (22, 1, '我操', '2024-11-14 07:23:44', '2024-11-14 07:23:44');
INSERT INTO `tg_draw_bot`.`text` (`id`, `language_id`, `content`, `created_at`, `updated_at`) VALUES (23, 2, '162', '2024-11-14 07:23:44', '2024-11-14 07:23:44');

INSERT INTO `tg_draw_bot`.`resource_text` (`resource_id`, `text_id`) VALUES (1, 16);
INSERT INTO `tg_draw_bot`.`resource_text` (`resource_id`, `text_id`) VALUES (1, 17);
INSERT INTO `tg_draw_bot`.`resource_text` (`resource_id`, `text_id`) VALUES (2, 18);
INSERT INTO `tg_draw_bot`.`resource_text` (`resource_id`, `text_id`) VALUES (2, 19);
INSERT INTO `tg_draw_bot`.`resource_text` (`resource_id`, `text_id`) VALUES (3, 20);
INSERT INTO `tg_draw_bot`.`resource_text` (`resource_id`, `text_id`) VALUES (3, 21);
INSERT INTO `tg_draw_bot`.`resource_text` (`resource_id`, `text_id`) VALUES (4, 22);
INSERT INTO `tg_draw_bot`.`resource_text` (`resource_id`, `text_id`) VALUES (4, 23);


INSERT INTO `tg_draw_bot`.`card_pool` (`id`, `start_at`, `end_at`, `is_open`, `resource_id`, `created_at`, `updated_at`) VALUES (1, '2024-10-18 00:00:00', '2024-12-13 00:00:00', 0, 1, '2024-11-20 01:11:55', '2024-11-20 01:11:55');
INSERT INTO `tg_draw_bot`.`card_pool` (`id`, `start_at`, `end_at`, `is_open`, `resource_id`, `created_at`, `updated_at`) VALUES (2, '2024-10-18 00:00:00', '2024-12-13 00:00:00', 0, 3, '2024-11-20 01:12:02', '2024-11-20 01:12:02');
INSERT INTO `tg_draw_bot`.`card_pool` (`id`, `start_at`, `end_at`, `is_open`, `resource_id`, `created_at`, `updated_at`) VALUES (3, '2024-10-18 00:00:00', '2024-12-13 00:00:00', 0, NULL, '2024-11-20 01:12:06', '2024-11-20 01:12:06');


INSERT INTO `tg_draw_bot`.`card_pool_text` (`card_pool_id`, `text_id`) VALUES (1, 24);
INSERT INTO `tg_draw_bot`.`card_pool_text` (`card_pool_id`, `text_id`) VALUES (1, 25);
INSERT INTO `tg_draw_bot`.`card_pool_text` (`card_pool_id`, `text_id`) VALUES (2, 26);
INSERT INTO `tg_draw_bot`.`card_pool_text` (`card_pool_id`, `text_id`) VALUES (2, 27);
INSERT INTO `tg_draw_bot`.`card_pool_text` (`card_pool_id`, `text_id`) VALUES (3, 28);
INSERT INTO `tg_draw_bot`.`card_pool_text` (`card_pool_id`, `text_id`) VALUES (3, 29);


INSERT INTO `tg_draw_bot`.`text` (`id`, `language_id`, `content`, `created_at`, `updated_at`) VALUES (24, 1, '超讚捏捏卡池', '2024-11-20 01:11:54', '2024-11-20 01:11:54');
INSERT INTO `tg_draw_bot`.`text` (`id`, `language_id`, `content`, `created_at`, `updated_at`) VALUES (25, 2, 'sexy boobs', '2024-11-20 01:11:54', '2024-11-20 01:11:54');
INSERT INTO `tg_draw_bot`.`text` (`id`, `language_id`, `content`, `created_at`, `updated_at`) VALUES (26, 1, '超讚捏捏卡池', '2024-11-20 01:12:01', '2024-11-20 01:12:01');
INSERT INTO `tg_draw_bot`.`text` (`id`, `language_id`, `content`, `created_at`, `updated_at`) VALUES (27, 2, 'sexy boobs', '2024-11-20 01:12:01', '2024-11-20 01:12:01');
INSERT INTO `tg_draw_bot`.`text` (`id`, `language_id`, `content`, `created_at`, `updated_at`) VALUES (28, 1, '超讚捏捏卡池', '2024-11-20 01:12:05', '2024-11-20 01:12:05');
INSERT INTO `tg_draw_bot`.`text` (`id`, `language_id`, `content`, `created_at`, `updated_at`) VALUES (29, 2, 'sexy boobs', '2024-11-20 01:12:05', '2024-11-20 01:12:05');



INSERT INTO `tg_draw_bot`.`card` (`id`, `card_pool_id`, `total_amount`, `drop_rate`, `resource_id`) VALUES (1, 1, NULL, 1.00, 1);
INSERT INTO `tg_draw_bot`.`card` (`id`, `card_pool_id`, `total_amount`, `drop_rate`, `resource_id`) VALUES (2, 1, NULL, 1.00, 4);
INSERT INTO `tg_draw_bot`.`card` (`id`, `card_pool_id`, `total_amount`, `drop_rate`, `resource_id`) VALUES (3, 1, NULL, 5.00, 3);
INSERT INTO `tg_draw_bot`.`card` (`id`, `card_pool_id`, `total_amount`, `drop_rate`, `resource_id`) VALUES (4, 1, NULL, 1.00, 2);

