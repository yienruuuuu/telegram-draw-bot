package io.github.yienruuuuu.bean.enums;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Eric.Lee
 * Date: 2024/11/13
 */
public enum RarityType {
    SR,
    SSR,
    UR;

    // 靜態方法，返回所有列舉值名稱的串接字串
    public static String getAllRarityTypesAsString() {
        return Arrays.stream(values())
                .map(RarityType::name)
                .collect(Collectors.joining("/"));
    }
}
