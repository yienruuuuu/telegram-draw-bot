package io.github.yienruuuuu.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Eric.Lee
 * Date: 2024/11/18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCardPoolPicDto {
    private Integer cardPoolId;
    private Integer page;
    private String resourceUniqueId;
}
