package io.github.yienruuuuu.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.yienruuuuu.bean.entity.CardPool;
import io.github.yienruuuuu.bean.entity.Language;
import io.github.yienruuuuu.bean.entity.Resource;
import io.github.yienruuuuu.bean.entity.Text;
import io.github.yienruuuuu.bean.enums.RarityType;
import io.github.yienruuuuu.service.exception.ApiException;
import io.github.yienruuuuu.service.exception.SysCode;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
        template.put("rarityType", RarityType.getAllRarityTypesAsString());
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
        template.put("startAt", cardPool == null ? "20YY-MM-DD" : DATE_FORMATTER.format(cardPool.getStartAt()));
        template.put("endAt", cardPool == null ? "20YY-MM-DD" : DATE_FORMATTER.format(cardPool.getEndAt()));
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

}