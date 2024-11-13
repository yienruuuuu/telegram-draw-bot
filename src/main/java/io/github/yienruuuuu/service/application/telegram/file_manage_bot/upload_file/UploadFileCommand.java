package io.github.yienruuuuu.service.application.telegram.file_manage_bot.upload_file;

import io.github.yienruuuuu.bean.dto.FileDataDto;
import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.enums.FileType;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
public interface UploadFileCommand {
    void execute(Update update, Bot botEntity, FileDataDto dto);

    FileType getFileType();
}
