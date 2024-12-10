package io.github.yienruuuuu.service.application.telegram.main_bot.command;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.entity.Language;
import io.github.yienruuuuu.bean.entity.User;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.application.telegram.main_bot.MainBotCommand;
import io.github.yienruuuuu.service.business.AnnouncementService;
import io.github.yienruuuuu.service.business.LanguageService;
import io.github.yienruuuuu.service.business.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * change language指令處理器
 *
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Slf4j
@Component
public class ChangeLanguage extends BaseCommand implements MainBotCommand {

    public ChangeLanguage(UserService userService, LanguageService languageService, TelegramBotClient telegramBotClient, AnnouncementService announcementService) {
        super(userService, languageService, telegramBotClient, announcementService);
    }


    @Override
    public void execute(Update update, Bot mainBotEntity) {
        var chatId = update.getCallbackQuery().getMessage().getChatId();
        var messageId = update.getCallbackQuery().getMessage().getMessageId();
        var languageCode = update.getCallbackQuery().getFrom().getLanguageCode();
        var callbackQueryId = update.getCallbackQuery().getId();
        CompletableFuture.runAsync(() -> telegramBotClient.send(AnswerCallbackQuery.builder().callbackQueryId(callbackQueryId).build(), mainBotEntity));
        var userId = String.valueOf(update.getCallbackQuery().getFrom().getId());
        User user = super.checkAndGetUserIfExists(userId, mainBotEntity, chatId, languageCode);

        var dataList = update.getCallbackQuery().getData().split(" ");
        if (dataList.length == 2) {
            handleChangeLanguage(dataList, user, String.valueOf(chatId), messageId, mainBotEntity);
        } else {
            CompletableFuture.runAsync(() ->
                    telegramBotClient.send(
                            EditMessageReplyMarkup.builder().chatId(chatId).messageId(messageId).replyMarkup(createInlineKeyBoard()).build(), mainBotEntity)
            );
        }
    }

    @Override
    public String getCommandName() {
        return "/change_language";
    }

    private void handleChangeLanguage(String[] dataList, User user, String chatId, Integer messageId, Bot mainBotEntity) {
        var languageId = dataList[1];
        var language = languageService.findById(Integer.valueOf(languageId));
        user.setLanguage(language);
        User userAfterSave = userService.save(user);
        String mess = "Language :" + userAfterSave.getLanguage().getFlagPattern() + userAfterSave.getLanguage().getOriginalLanguageName();
        log.info("User {} changed language to {}.", user.getId(), language.getLanguageCode());
        CompletableFuture.runAsync(() -> telegramBotClient.send(
                EditMessageText.builder()
                        .chatId(chatId)
                        .messageId(messageId)
                        .text(mess)
                        .replyMarkup(super.createInlineKeyBoardForLanguageSetting())
                        .build(), mainBotEntity));
    }

    /**
     * 創建並返回按鈕行，一行最多顯示兩個按鈕
     */
    private InlineKeyboardMarkup createInlineKeyBoard() {
        List<Language> languageList = languageService.findAllLanguages();
        List<InlineKeyboardRow> rows = new ArrayList<>();
        InlineKeyboardRow currentRow = new InlineKeyboardRow();
        for (int i = 0; i < languageList.size(); i++) {
            Language language = languageList.get(i);
            InlineKeyboardButton button = super.createInlineButton(
                    language.getFlagPattern() + language.getLanguageCode(), "/change_language " + language.getId());
            currentRow.add(button);
            // 每行超過兩個按鈕時，新增到 rows 並創建新的一行
            if ((i + 1) % 2 == 0 || i == languageList.size() - 1) {
                rows.add(currentRow);
                currentRow = new InlineKeyboardRow();
            }
        }
        // 將所有列加入列表
        return new InlineKeyboardMarkup(rows);
    }
}