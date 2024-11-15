package io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.command;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.entity.Resource;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.DataManageCommand;
import io.github.yienruuuuu.service.business.AnnouncementService;
import io.github.yienruuuuu.service.business.LanguageService;
import io.github.yienruuuuu.service.business.ResourceService;
import io.github.yienruuuuu.service.business.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

/**
 * start指令處理器
 *
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Slf4j
@Component
public class ListResourceByTimeDescCommand extends DataManageBaseCommand implements DataManageCommand {


    public ListResourceByTimeDescCommand(UserService userService, LanguageService languageService, TelegramBotClient telegramBotClient, AnnouncementService announcementService, ResourceService resourceService) {
        super(userService, languageService, telegramBotClient, announcementService, resourceService);
    }

    @Override
    public void execute(Update update, Bot fileManageBot) {
        var userId = String.valueOf(update.getMessage().getFrom().getId());
        var chatId = String.valueOf(update.getMessage().getChatId());
        //檢查操作權限
        checkUsersPermission(userId, chatId, fileManageBot);

        // 提取分頁參數，默認為第 1 頁
        int pageNumber = super.extractPageNumber(update.getMessage().getText()) - 1; // Pageable 的頁碼從 0 開始
        int pageSize = 10;

        Page<Resource> resourcePage = resourceService.findAllByPage(PageRequest.of(pageNumber, pageSize));

        if (resourcePage.isEmpty()) {
            telegramBotClient.send(SendMessage.builder()
                    .chatId(chatId)
                    .text("No more resources to display.")
                    .build(), fileManageBot);
            return;
        }
        //傳送resource訊息
        resourcePage.getContent().forEach(resource -> createMediaMessageAndSendMedia(resource, chatId, fileManageBot));

        // 附加分頁信息
        String pageInfo = String.format(" %d of %d", resourcePage.getNumber() + 1, resourcePage.getTotalPages());

        // 創建按鈕行和鍵盤
        ReplyKeyboardMarkup keyboard = ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow(getCommandName() + " " + (resourcePage.getNumber() + 2)))
                .resizeKeyboard(true)
                .build();

        // 傳送當前頁數及下頁按鈕
        telegramBotClient.send(SendMessage.builder()
                .chatId(chatId)
                .text("Page :" + pageInfo)
                .replyMarkup(keyboard)
                .build(), fileManageBot);
    }

    @Override
    public String getCommandName() {
        return "/list_resource_by_time_desc";
    }

    /**
     * 根據資源類型創建對應的媒體消息
     */
    private void createMediaMessageAndSendMedia(Resource resource, String chatId, Bot fileManageBot) {
        switch (resource.getFileType()) {
            case PHOTO -> telegramBotClient.send(
                    SendPhoto.builder()
                            .chatId(chatId)
                            .photo(new InputFile(resource.getFileIdManageBot()))
                            .caption("`" + resource.getUniqueId() + "`")
                            .parseMode("MarkdownV2")
                            .build(),
                    fileManageBot
            );
            case VIDEO -> telegramBotClient.send(
                    SendVideo.builder()
                            .chatId(chatId)
                            .video(new InputFile(resource.getFileIdManageBot()))
                            .caption("`" + resource.getUniqueId() + "`")
                            .parseMode("MarkdownV2")
                            .build(),
                    fileManageBot
            );
            case GIF -> telegramBotClient.send(
                    SendAnimation.builder()
                            .chatId(chatId)
                            .animation(new InputFile(resource.getFileIdManageBot()))
                            .caption("`" + resource.getUniqueId() + "`")
                            .parseMode("MarkdownV2")
                            .build(),
                    fileManageBot
            );
            default -> throw new IllegalArgumentException("Unsupported FileType: " + resource.getFileType());
        }
    }
}