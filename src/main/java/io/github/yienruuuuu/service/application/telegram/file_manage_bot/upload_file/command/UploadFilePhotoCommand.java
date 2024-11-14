package io.github.yienruuuuu.service.application.telegram.file_manage_bot.upload_file.command;

import io.github.yienruuuuu.bean.dto.FileDataDto;
import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.entity.Resource;
import io.github.yienruuuuu.bean.enums.FileType;
import io.github.yienruuuuu.bean.enums.RarityType;
import io.github.yienruuuuu.config.AppConfig;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.application.telegram.file_manage_bot.upload_file.UploadFileCommand;
import io.github.yienruuuuu.service.business.LanguageService;
import io.github.yienruuuuu.service.business.ResourceService;
import io.github.yienruuuuu.service.business.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * start指令處理器
 *
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Slf4j
@Component
public class UploadFilePhotoCommand extends UploadFileBaseCommand implements UploadFileCommand {


    public UploadFilePhotoCommand(UserService userService, TelegramBotClient telegramBotClient, ResourceService resourceService, AppConfig appConfig, LanguageService languageService) {
        super(userService, telegramBotClient, resourceService, appConfig, languageService);
    }

    @Override
    public void execute(Update update, Bot fileBotEntity, FileDataDto fileDataDto) {
        String chatId = update.getMessage().getChatId().toString();
        Resource resource = Resource.builder()
                .rarityType(RarityType.NORMAL)
                .fileType(getFileType())
                .fileIdManageBot(fileDataDto.fileId())
                .uniqueId(fileDataDto.fileUniqueId())
                .hasBeenCardBefore(false)
                .build();
        Resource newResource = resourceService.save(resource);
        SendPhoto sendPhoto = SendPhoto.builder()
                .photo(new InputFile(fileDataDto.fileId()))
                .chatId(appConfig.getBotCommunicatorChatId())
                .caption(newResource.getUniqueId())
                .build();
        telegramBotClient.send(sendPhoto, fileBotEntity);
        //傳送完善resource的模板
        super.sendEditResourceTemplate(newResource, chatId, fileBotEntity);
    }

    @Override
    public FileType getFileType() {
        return FileType.PHOTO;
    }
}