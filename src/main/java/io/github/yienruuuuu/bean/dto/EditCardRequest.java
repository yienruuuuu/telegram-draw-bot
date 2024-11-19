package io.github.yienruuuuu.bean.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Eric.Lee
 * Date: 2024/11/13
 */
@Data
public class EditCardRequest {
    private Integer id;
    private BigDecimal dropRate;
}
