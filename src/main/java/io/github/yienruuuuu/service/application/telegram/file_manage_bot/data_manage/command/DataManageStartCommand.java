package io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.DataManageCommand;
import io.github.yienruuuuu.service.business.AnnouncementService;
import io.github.yienruuuuu.service.business.LanguageService;
import io.github.yienruuuuu.service.business.ResourceService;
import io.github.yienruuuuu.service.business.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * start指令處理器
 *
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Slf4j
@Component
public class DataManageStartCommand extends DataManageBaseCommand implements DataManageCommand {


    public DataManageStartCommand(ObjectMapper objectMapper, UserService userService, LanguageService languageService, TelegramBotClient telegramBotClient, AnnouncementService announcementService, ResourceService resourceService) {
        super(objectMapper, userService, languageService, telegramBotClient, announcementService, resourceService);
    }

    @Override
    public void execute(Update update, Bot mainBotEntity) {
        var userId = String.valueOf(update.getMessage().getFrom().getId());
        var chatId = String.valueOf(update.getMessage().getChatId());
        //檢查操作權限
        checkUsersPermission(userId, chatId, mainBotEntity);


    }

    @Override
    public String getCommandName() {
        return "/start";
    }

}