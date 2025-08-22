package io.github.yienruuuuu.service.business;

import io.github.yienruuuuu.bean.dto.HintSearchResult;
import io.github.yienruuuuu.bean.entity.Card;
import io.github.yienruuuuu.bean.entity.CardPool;
import io.github.yienruuuuu.bean.entity.Resource;
import io.github.yienruuuuu.bean.enums.CardPoolType;
import io.github.yienruuuuu.bean.enums.MatchResultType;
import io.github.yienruuuuu.service.exception.ApiException;
import io.github.yienruuuuu.service.exception.SysCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Eric.Lee
 * Date: 2025/8/22
 */
@Component
public class HintTagIndex {

    private final CardPoolService cardPoolService;
    private final Map<String, Resource> tagIndex = new HashMap<>();

    public HintTagIndex(CardPoolService cardPoolService) {
        this.cardPoolService = cardPoolService;
        this.buildIndex();
    }

    /**
     * 刷新提示索引，重新建立 tag -> Resource 的索引
     */
    public void refreshHintIndex() {
        tagIndex.clear();
        buildIndex();
    }


    /**
     * 初始化：建立 tag -> Resource的索引
     */
    private void buildIndex() {
        List<CardPool> pool = cardPoolService.findOpenCardPoolsByPoolType(CardPoolType.HINT);
        if (pool.isEmpty()) {
            return;
        }

        for (Card card : pool.get(0).getCards()) {
            Resource res = Optional.of(card.getResource())
                    .orElseThrow(() -> new ApiException(SysCode.HINT_IS_INCOMPLETE, card.getId()));
            String tags = Optional.of(res.getTags())
                    .orElseThrow(() -> new ApiException(SysCode.HINT_IS_INCOMPLETE, card.getId()));
            // 拆 tags
            String[] tagArr = tags.split("[,，\\s]+");

            for (String rawTag : tagArr) {
                String tag = rawTag.trim().toLowerCase();
                tagIndex.computeIfAbsent(tag, k -> res);
            }
        }
    }

    /**
     * 使用者輸入關鍵字，模糊比對 tags
     */
    public HintSearchResult searchByHint(String userInput) {

        if (StringUtils.isBlank(userInput)) {
            return new HintSearchResult(MatchResultType.NO_MATCH, null);
        }

        String keyword = userInput.toLowerCase();

        for (Map.Entry<String, Resource> entry : tagIndex.entrySet()) {
            String tag = entry.getKey();
            Resource res = entry.getValue();

            if (tag.equals(keyword)) {
                return new HintSearchResult(MatchResultType.MATCH, res);
            } else if (tag.contains(keyword) || keyword.contains(tag)) {
                return new HintSearchResult(MatchResultType.SUSPECT, res);
            }
        }

        return new HintSearchResult(MatchResultType.NO_MATCH, null);
    }
}
