package io.github.yienruuuuu.service.application.telegram.file_manage_bot;

import io.github.yienruuuuu.bean.dto.FileDataDto;
import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.enums.BotType;
import io.github.yienruuuuu.bean.enums.FileType;
import io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.dispatcher.DataManageCallbackDispatcher;
import io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.dispatcher.DataManageCommandDispatcher;
import io.github.yienruuuuu.service.application.telegram.file_manage_bot.upload_file.dispatcher.UploadFileDispatcher;
import io.github.yienruuuuu.service.business.BotService;
import io.github.yienruuuuu.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Comparator;

/**
 * @author Eric.Lee Date: 2024/10/16
 */

@Component
@Slf4j
public class FileManageBotConsumer implements LongPollingSingleThreadUpdateConsumer {
    private final BotService botService;
    //Dispatcher
    private final DataManageCommandDispatcher dataManageCommandDispatcher;
    private final DataManageCallbackDispatcher dataManageCallbackDispatcher;
    private final UploadFileDispatcher uploadFileDispatcher;

    @Autowired
    public FileManageBotConsumer(BotService botService, DataManageCommandDispatcher dataManageCommandDispatcher, DataManageCallbackDispatcher dataManageCallbackDispatcher, UploadFileDispatcher uploadFileDispatcher) {
        this.botService = botService;
        this.dataManageCommandDispatcher = dataManageCommandDispatcher;
        this.dataManageCallbackDispatcher = dataManageCallbackDispatcher;
        this.uploadFileDispatcher = uploadFileDispatcher;
    }

    @Override
    public void consume(Update update) {
        JsonUtils.parseJsonAndPrintLog("FILE MANAGE BOT CONSUMER收到Update訊息", update);
        Bot fileManageBotEntity = botService.findByBotType(BotType.FILE_MANAGE);

        if (update.hasMessage()) {
            handleMessage(update, fileManageBotEntity);
        } else if (update.hasCallbackQuery()) {
            dataManageCallbackDispatcher.dispatch(update, fileManageBotEntity);
        } else {
            log.warn("FILE MANAGE BOT CONSUMER收到不支援的更新類型");
        }
    }

    /**
     * 處理hasMessage下的訊息
     */
    private void handleMessage(Update update, Bot fileManageBotEntity) {
        if (update.getMessage().hasText()) {
            dataManageCommandDispatcher.dispatch(update, fileManageBotEntity);
        } else if (hasMedia(update)) {
            uploadFileDispatcher.dispatch(update, fileManageBotEntity, handleMedia(update));
        } else {
            log.warn("FILE MANAGE BOT未處理的消息類型");
        }
    }

    /**
     * 檢查是否有媒體
     */
    private boolean hasMedia(Update update) {
        return update.getMessage().hasPhoto() || update.getMessage().hasAnimation() || update.getMessage().hasVideo();
    }

    /**
     * 處理媒體
     */
    private FileDataDto handleMedia(Update update) {
        String fileId;
        String fileUniqueId;
        FileType fileType;

        if (update.getMessage().hasPhoto()) {
            PhotoSize photo = update.getMessage().getPhoto().stream()
                    .max(Comparator.comparingInt(PhotoSize::getFileSize))
                    .orElseThrow(() -> new IllegalArgumentException("沒有照片"));
            fileId = photo.getFileId();
            fileUniqueId = photo.getFileUniqueId();
            fileType = FileType.PHOTO;
        } else if (update.getMessage().hasAnimation()) {
            fileId = update.getMessage().getAnimation().getFileId();
            fileUniqueId = update.getMessage().getAnimation().getFileUniqueId();
            fileType = FileType.GIF;
        } else if (update.getMessage().hasVideo()) {
            fileId = update.getMessage().getVideo().getFileId();
            fileUniqueId = update.getMessage().getVideo().getFileUniqueId();
            fileType = FileType.VIDEO;
        } else {
            throw new IllegalArgumentException("不支持的媒體類型");
        }
        return createFileData(fileType, fileId, fileUniqueId);
    }

    /**
     * 通用方法：根據文件對象創建 FileDataDto
     */
    private FileDataDto createFileData(FileType fileType, String fileId, String fileUniqueId) {
        return new FileDataDto(fileType, fileId, fileUniqueId);
    }
}

