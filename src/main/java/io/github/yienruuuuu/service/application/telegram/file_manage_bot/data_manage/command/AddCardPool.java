package io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.command;

import io.github.yienruuuuu.bean.dto.AddCardPoolRequest;
import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.entity.CardPool;
import io.github.yienruuuuu.bean.entity.Resource;
import io.github.yienruuuuu.bean.enums.CardPoolType;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.DataManageCommand;
import io.github.yienruuuuu.service.business.*;
import io.github.yienruuuuu.service.exception.ApiException;
import io.github.yienruuuuu.service.exception.SysCode;
import io.github.yienruuuuu.utils.JsonUtils;
import io.github.yienruuuuu.utils.TemplateGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.Instant;

/**
 * start指令處理器
 *
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Slf4j
@Component
public class AddCardPool extends DataManageBaseCommand implements DataManageCommand {
    private final CardPoolService cardPoolService;

    public AddCardPool(UserService userService, LanguageService languageService, TelegramBotClient telegramBotClient, AnnouncementService announcementService, ResourceService resourceService, CardPoolService cardPoolService) {
        super(userService, languageService, telegramBotClient, announcementService, resourceService);
        this.cardPoolService = cardPoolService;
    }

    @Override
    public void execute(Update update, Bot fileManageBot) {
        var userId = String.valueOf(update.getMessage().getFrom().getId());
        var chatId = String.valueOf(update.getMessage().getChatId());
        //檢查操作權限
        checkUsersPermission(userId, chatId, fileManageBot);
        AddCardPoolRequest addCardPoolRequest = this.parseJsonToDto(update);
        if (addCardPoolRequest == null) {
            sendEditResourceTemplate(chatId, fileManageBot);
            return;
        }
        //查詢資源
        Resource res = StringUtils.isBlank(addCardPoolRequest.getResourceId())
                ? null
                : resourceService.findByUniqueId(addCardPoolRequest.getResourceId())
                .orElseThrow(() -> new ApiException(SysCode.RESOURCE_NOT_FOUNT));
        //新增卡池
        String startAtIso = addCardPoolRequest.getStartAt() + "T08:00:00+08:00";
        String endAtIso = addCardPoolRequest.getEndAt() + "T08:00:00+08:00";
        CardPool newPool = CardPool.builder()
                .isOpen(false)
                .startAt(Instant.parse(startAtIso))
                .endAt(Instant.parse(endAtIso))
                .resource(res)
                .texts(super.convertToTextEntities(addCardPoolRequest.getTexts()))
                .cardPoolType(CardPoolType.valueOf(addCardPoolRequest.getCardPoolType()))
                .build();
        CardPool pool = cardPoolService.save(newPool);
        telegramBotClient.send(
                SendMessage.builder().chatId(chatId).text("已儲存, 卡池 id = " + pool.getId()).build(),
                fileManageBot
        );
    }

    @Override
    public String getCommandName() {
        return "/add_card_pool";
    }

    /**
     * 傳送完善resource的json模板
     */
    private void sendEditResourceTemplate(String chatId, Bot fileBotEntity) {
        String template = TemplateGenerator.generateCardPoolTemplate(null, languageService.findAllLanguages());
        SendMessage askForCompleteResourceSetting = SendMessage.builder()
                .chatId(chatId)
                .text("`/add_card_pool " + template + "`")
                .parseMode("MarkdownV2")
                .build();
        telegramBotClient.send(askForCompleteResourceSetting, fileBotEntity);
    }

    /**
     * 將 text.JSON 字串轉換為 EditResourceRequest DTO
     */
    private AddCardPoolRequest parseJsonToDto(Update update) {
        String text = update.getMessage().getText();
        String jsonString = text.substring(getCommandName().length()).trim();
        // 檢查 JSON 是否為空
        if (jsonString.isEmpty()) {
            return null;
        }
        return JsonUtils.parseJsonToTargetDto(jsonString, AddCardPoolRequest.class);
    }
}