package io.github.yienruuuuu.service.application.telegram.file_manage_bot.upload_file.dispatcher;

import io.github.yienruuuuu.bean.dto.FileDataDto;
import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.enums.FileType;
import io.github.yienruuuuu.service.application.telegram.file_manage_bot.upload_file.UploadFileCommand;
import io.github.yienruuuuu.service.application.telegram.file_manage_bot.upload_file.command.UploadFileGifCommand;
import io.github.yienruuuuu.service.application.telegram.file_manage_bot.upload_file.command.UploadFilePhotoCommand;
import io.github.yienruuuuu.service.application.telegram.file_manage_bot.upload_file.command.UploadFileVideoCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.EnumMap;

/**
 * 命令類適配收到的telegram命令並執行(execute())相對應的功能
 *
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Component
public class UploadFileDispatcher {
    private final EnumMap<FileType, UploadFileCommand> commandMap = new EnumMap<>(FileType.class);

    @Autowired
    public UploadFileDispatcher(UploadFileGifCommand uploadGifCommand, UploadFilePhotoCommand uploadFilePhotoCommand, UploadFileVideoCommand uploadFileVideoCommand) {
        commandMap.put(FileType.GIF, uploadGifCommand);
        commandMap.put(FileType.PHOTO, uploadFilePhotoCommand);
        commandMap.put(FileType.VIDEO, uploadFileVideoCommand);
    }

    public void dispatch(Update update, Bot fileManageBotEntity, FileDataDto fileDataDto) {
        UploadFileCommand command = commandMap.get(fileDataDto.fileType());
        if (command != null) {
            command.execute(update, fileManageBotEntity, fileDataDto);
        }
    }
}
