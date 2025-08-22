UPDATE language
SET original_language_name = '繁體中文', flag_pattern = '🇹🇼'
WHERE language_code = 'zh-hant';

UPDATE language
SET original_language_name = 'English', flag_pattern = '🇺🇸'
WHERE language_code = 'en';

UPDATE language
SET original_language_name = '简体中文', flag_pattern = '🇨🇳'
WHERE language_code = 'zh-hans';

UPDATE language
SET original_language_name = 'العربية', flag_pattern = '🇦🇪'
WHERE language_code = 'ar';

UPDATE language
SET original_language_name = 'Deutsch', flag_pattern = '🇩🇪'
WHERE language_code = 'de';

UPDATE language
SET original_language_name = 'Italiano', flag_pattern = '🇮🇹'
WHERE language_code = 'it';
