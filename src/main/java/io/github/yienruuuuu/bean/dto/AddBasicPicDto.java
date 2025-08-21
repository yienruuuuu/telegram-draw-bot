package io.github.yienruuuuu.bean.dto;

import io.github.yienruuuuu.bean.enums.BasicPicType;
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
public class AddBasicPicDto {
    private BasicPicType t;
    //page
    private Integer pg;
    //resource id
    private Integer rId;
}
