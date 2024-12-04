package io.github.yienruuuuu.service.application.telegram.main_bot;

import io.github.yienruuuuu.bean.entity.Resource;
import io.github.yienruuuuu.config.AppConfig;
import io.github.yienruuuuu.service.business.ResourceService;
import io.github.yienruuuuu.service.exception.ApiException;
import io.github.yienruuuuu.service.exception.SysCode;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Comparator;

/**
 * @author Eric.Lee
 * Date: 2024/11/13
 */
@Component
public class ChannelPostHandler {
    private final ResourceService resourceService;
    private final AppConfig appConfig;

    public ChannelPostHandler(ResourceService resourceService, AppConfig appConfig) {
        this.resourceService = resourceService;
        this.appConfig = appConfig;
    }

    public void handleChannelPost(Update update) {
        String uniqueId = update.getChannelPost().getCaption();
        var channelId = String.valueOf(update.getChannelPost().getChatId());
        //檢核頻道是否正確
        if (!channelId.equals(appConfig.getBotCommunicateChannelChatId())) return;
        String fileId = getFileId(update);
        Resource resource = resourceService.findByUniqueId(uniqueId)
                .orElseThrow(() -> new ApiException(SysCode.RESOURCE_NOT_FOUNT));
        resource.setFileIdMainBot(fileId);
        resourceService.save(resource);
    }


    private String getFileId(Update update) {
        if (update.getChannelPost().hasPhoto()) {
            return update.getChannelPost().getPhoto().stream()
                    .max(Comparator.comparingInt(PhotoSize::getFileSize))
                    .orElseThrow(() -> new IllegalArgumentException("沒有照片"))
                    .getFileId();
        }
        if (update.getChannelPost().hasAnimation()) {
            return update.getChannelPost().getAnimation().getFileId();
        }
        if (update.getChannelPost().hasVideo()) {
            return update.getChannelPost().getVideo().getFileId();
        }
        throw new ApiException(SysCode.RESOURCE_NOT_FOUNT);
    }
}
