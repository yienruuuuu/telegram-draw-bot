package io.github.yienruuuuu.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.yienruuuuu.bean.entity.Language;
import io.github.yienruuuuu.bean.entity.Resource;
import io.github.yienruuuuu.bean.entity.Text;
import io.github.yienruuuuu.bean.enums.RarityType;
import io.github.yienruuuuu.service.exception.ApiException;
import io.github.yienruuuuu.service.exception.SysCode;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Eric.Lee
 * Date: 2024/11/13
 */
@Slf4j
public class ResourceTemplateGenerator {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private ResourceTemplateGenerator() {
        // 拋出異常了防止透過反射呼叫私有建構子
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }


    public static String generateTemplateWithData(Resource resource, List<Language> languages) {
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
}
