package io.github.yienruuuuu.bean.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author Eric.Lee
 * Date: 2024/11/13
 */
@Data
public class EditResourceRequest {
    private String uniqueId;
    private String rarityType;
    private String tags;
    private List<Map<String, String>> texts; // 對應多語言的內容
}
