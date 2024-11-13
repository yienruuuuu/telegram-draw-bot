package io.github.yienruuuuu.bean.dto;

import io.github.yienruuuuu.bean.enums.FileType;

/**
 * @author Eric.Lee
 * Date: 2024/11/13
 */
public record FileDataDto(FileType fileType, String fileId, String fileUniqueId) {
}
