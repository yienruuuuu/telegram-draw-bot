package io.github.yienruuuuu.bean.dto;

import lombok.Data;

/**
 * @author Eric.Lee
 * Date: 2024/11/13
 */
@Data
public class AddCheatCodeRequest {
    private String code;
    private Integer pointAmount;
    private String validFrom;
    private String validTo;
    private Integer maxUsage;
    private boolean isActive;
}
