package io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.command;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.entity.Language;
import io.github.yienruuuuu.bean.entity.User;
import io.github.yienruuuuu.bean.enums.AnnouncementType;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.DataManageCommand;
import io.github.yienruuuuu.service.business.AnnouncementService;
import io.github.yienruuuuu.service.business.LanguageService;
import io.github.yienruuuuu.service.business.ResourceService;
import io.github.yienruuuuu.service.business.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * start指令處理器
 *
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Slf4j
@Component
public class StartManage extends DataManageBaseCommand implements DataManageCommand {

    public StartManage(UserService userService, LanguageService languageService, TelegramBotClient telegramBotClient, AnnouncementService announcementService, ResourceService resourceService) {
        super(userService, languageService, telegramBotClient, announcementService, resourceService);
    }

    @Override
    public void execute(Update update, Bot fileManageBot) {
        var userId = String.valueOf(update.getMessage().getFrom().getId());
        var chatId = String.valueOf(update.getMessage().getChatId());
        //檢查操作權限
        checkUsersPermission(userId, chatId, fileManageBot);
        //查詢操作者
        User user = userService.findByTelegramUserId(userId);
        //傳送操作幫助
        telegramBotClient.send(createSendMessageOfAnnouncement(user, chatId), fileManageBot);
    }

    @Override
    public String getCommandName() {
        return "/start";
    }

    /**
     * 根據使用者的語言，選擇對應的開始訊息並傳送給使用者
     */
    private SendMessage createSendMessageOfAnnouncement(User newUser, String chatId) {
        Language language = newUser.getLanguage();
        String mess = super.getAnnouncementMessage(AnnouncementType.FILE_MANAGE_START_MESSAGE, language)
                .orElse("can't found start message");
        return SendMessage.builder()
                .chatId(chatId)
                .text(mess)
                .build();
    }

}