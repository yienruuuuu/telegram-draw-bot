package io.github.yienruuuuu.bean.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author Eric.Lee
 * Date: 2024/11/13
 */
@Data
public class EditCardPoolRequest {
    private String id;
    private String startAt;
    private String endAt;
    private boolean isOpen;
    private String resourceId;
    private List<Map<String, String>> texts;
}
