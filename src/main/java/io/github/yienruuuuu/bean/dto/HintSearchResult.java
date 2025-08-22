package io.github.yienruuuuu.bean.dto;

import io.github.yienruuuuu.bean.entity.Resource;
import io.github.yienruuuuu.bean.enums.MatchResultType;

/**
 * @author Eric.Lee
 * Date: 2025/8/22
 */
public record HintSearchResult(
        MatchResultType resultType,
        Resource resource
) {
}