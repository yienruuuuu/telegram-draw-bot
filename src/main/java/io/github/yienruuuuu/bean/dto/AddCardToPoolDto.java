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
public class AddCardToPoolDto {
    //card pool id
    private Integer cpId;
    //page
    private Integer pg;
    //resource id
    private Integer rId;
}
