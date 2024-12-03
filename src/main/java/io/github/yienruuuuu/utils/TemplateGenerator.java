package io.github.yienruuuuu.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.yienruuuuu.bean.entity.*;
import io.github.yienruuuuu.service.exception.ApiException;
import io.github.yienruuuuu.service.exception.SysCode;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * @author Eric.Lee
 * Date: 2024/11/13
 */
@Slf4j
public class TemplateGenerator {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());

    private TemplateGenerator() {
        // 拋出異常了防止透過反射呼叫私有建構子
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }


    /**
     * 產生resource的json模板
     */
    public static String generateResourceTemplate(Resource resource, List<Language> languages) {
        ObjectNode template = objectMapper.createObjectNode();
        template.put("uniqueId", resource.getUniqueId());
        template.put("rarityType", resource.getRarityType().name());
        template.put("tags", resource.getTags());

        ArrayNode texts = template.putArray("texts");

        // 如果 resource.getTexts() 為 null，使用空列表代替
        List<Text> resourceTexts = resource.getTexts() != null ? resource.getTexts() : List.of();

        // 遍歷 languages，根據每個語言生成文本節點
        languages.forEach(language -> {
            ObjectNode textNode = texts.addObject();
            // 根據 language 查找對應的文本內容
            String content = resourceTexts.stream()
                    .filter(text -> text.getLanguage().getLanguageCode().equals(language.getLanguageCode()))
                    .map(Text::getContent)
                    .findFirst()
                    .orElse("");
            textNode.put(language.getLanguageCode(), content);
        });

        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(template);
        } catch (Exception e) {
            throw new ApiException(SysCode.CREATE_TEMPLATE_ERROR, e);
        }
    }

    /**
     * 產生resource的json模板
     */
    public static String generateCardPoolTemplate(CardPool cardPool, List<Language> languages) {
        ObjectNode template = objectMapper.createObjectNode();
        if (cardPool != null) template.put("id", cardPool.getId());
        String defaultDate = DATE_FORMATTER.format(LocalDateTime.now());
        template.put("startAt", cardPool == null ? defaultDate : DATE_FORMATTER.format(cardPool.getStartAt()));
        template.put("endAt", cardPool == null ? defaultDate : DATE_FORMATTER.format(cardPool.getEndAt()));
        template.put("isOpen", cardPool != null && cardPool.isOpen());
        template.put("resourceId", cardPool == null ? null : cardPool.getResource().getUniqueId());

        ArrayNode texts = template.putArray("texts");

        // 如果 resource.getTexts() 為 null，使用空列表代替
        List<Text> textList = (cardPool != null && cardPool.getTexts() != null)
                ? cardPool.getTexts()
                : List.of();

        // 遍歷 languages，根據每個語言生成文本節點
        languages.forEach(language -> {
            ObjectNode textNode = texts.addObject();
            // 根據 language 查找對應的文本內容
            String content = textList.stream()
                    .filter(text -> text.getLanguage().getLanguageCode().equals(language.getLanguageCode()))
                    .map(Text::getContent)
                    .findFirst()
                    .orElse("");
            textNode.put(language.getLanguageCode(), content);
        });

        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(template);
        } catch (Exception e) {
            throw new ApiException(SysCode.CREATE_TEMPLATE_ERROR, e);
        }
    }


    /**
     * 產生card的json模板
     */
    public static String generateCardTemplate(Card card) {
        ObjectNode template = objectMapper.createObjectNode();
        template.put("id", card.getId());
        template.put("dropRate", card.getDropRate());

        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(template);
        } catch (Exception e) {
            throw new ApiException(SysCode.CREATE_TEMPLATE_ERROR, e);
        }
    }

    /**
     * 產生cheat code的json模板
     */
    public static String generateCheatCodeTemplate(CheatCode cheatCode) {
        ObjectNode template = objectMapper.createObjectNode();
        String defaultDate = DATE_FORMATTER.format(LocalDateTime.now());
        template.put("code", cheatCode == null ? "/cheat_code " + UUID.randomUUID() : cheatCode.getCode());
        template.put("pointAmount", cheatCode == null ? 10 : cheatCode.getPointAmount());
        template.put("validFrom", cheatCode == null ? defaultDate : DATE_FORMATTER.format(cheatCode.getValidFrom()));
        template.put("validTo", cheatCode == null ? defaultDate : DATE_FORMATTER.format(cheatCode.getValidTo()));
        template.put("maxUsage", cheatCode == null ? null : cheatCode.getMaxUsage());
        template.put("isActive", cheatCode == null ? "true" : cheatCode.getIsActive().toString());


        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(template);
        } catch (Exception e) {
            throw new ApiException(SysCode.CREATE_TEMPLATE_ERROR, e);
        }
    }


}
