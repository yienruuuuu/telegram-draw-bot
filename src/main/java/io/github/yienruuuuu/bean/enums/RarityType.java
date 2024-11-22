package io.github.yienruuuuu.bean.enums;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Eric.Lee
 * Date: 2024/11/13
 */
@Getter
public enum RarityType {
    SR(40),
    SSR(35),
    UR(25);

    private final BigDecimal defaultDropRate;

    // 修正建構子，將 int 轉換為 BigDecimal
    private RarityType(int defaultDropRate) {
        this.defaultDropRate = BigDecimal.valueOf(defaultDropRate);
    }


    // 靜態方法，返回所有列舉值名稱的串接字串
    public static String getAllRarityTypesAsString() {
        return Arrays.stream(values())
                .map(RarityType::name)
                .collect(Collectors.joining("/"));
    }
}
