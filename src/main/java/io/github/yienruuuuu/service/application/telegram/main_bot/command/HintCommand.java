package io.github.yienruuuuu.service.application.telegram.main_bot.command;

import io.github.yienruuuuu.bean.dto.HintSearchResult;
import io.github.yienruuuuu.bean.entity.*;
import io.github.yienruuuuu.bean.enums.BasicPicType;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.application.telegram.main_bot.MainBotCommand;
import io.github.yienruuuuu.service.business.*;
import io.github.yienruuuuu.service.exception.ApiException;
import io.github.yienruuuuu.service.exception.SysCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

/**
 * Hint指令處理器
 *
 * @author Eric.Lee
 * Date: 2025/08/22
 */
@Slf4j
@Component
public class HintCommand extends BaseCommand implements MainBotCommand {
    private final BasicPicService basicPicService;
    private final HintTagIndex hintTagIndex;

    public HintCommand(
            UserService userService,
            LanguageService languageService,
            TelegramBotClient telegramBotClient,
            AnnouncementService announcementService,
            BasicPicService basicPicService,
            HintTagIndex hintTagIndex
    ) {
        super(userService, languageService, telegramBotClient, announcementService);
        this.basicPicService = basicPicService;
        this.hintTagIndex = hintTagIndex;
    }


    @Override
    public void execute(Update update, Bot mainBotEntity) {
        var userId = String.valueOf(update.getMessage().getFrom().getId());
        var chatId = String.valueOf(update.getMessage().getChatId());
        // 查詢用戶
        User user = Optional.of(userService.findByTelegramUserId(userId))
                .orElseThrow(() -> new ApiException(SysCode.USER_NOT_FOUND));

        //若沒有攜帶參數，則提示如何使用
        String somethingInput = this.getStringInHint(update);
        if (StringUtils.isBlank(somethingInput)) {
            this.sendHowToUseHint(chatId, mainBotEntity, user.getLanguage());
            return;
        }

        //使用者輸入關鍵字，模糊比對 tags
        HintSearchResult result = hintTagIndex.searchByHint(somethingInput);
        //送出對應的回復
        this.sendHintResponse(chatId, mainBotEntity, user.getLanguage(), result);
    }

    @Override
    public String getCommandName() {
        return "/hint";
    }

    /**
     * 根據搜尋結果發送提示回應
     */
    private void sendHintResponse(
            String chatId,
            Bot mainBotEntity,
            Language language,
            HintSearchResult result
    ) {
        Resource resource = result.resource();
        String message = null;

        switch (result.resultType()) {

            case MATCH -> {
                message = resource.getTexts().stream()
                        .filter(t -> t.getLanguage().equals(language))
                        .findFirst()
                        .map(Text::getContent)
                        .orElseGet(() ->
                                resource.getTexts().stream()
                                        .filter(t -> "en".equalsIgnoreCase(t.getLanguage().getLanguageCode()))
                                        .findFirst()
                                        .map(Text::getContent)
                                        .orElse(null)
                        );
                this.sendMsgWithResource(chatId, mainBotEntity, resource, message);
            }

            case SUSPECT -> {
                BasicPic pic = basicPicService.findByType(BasicPicType.SUGGESTION_HINT);

                message = pic.getResource().getTexts().stream()
                        .filter(t -> t.getLanguage().equals(language))
                        .findFirst()
                        .map(Text::getContent)
                        .orElseGet(() ->
                                resource.getTexts().stream()
                                        .filter(t -> "en".equalsIgnoreCase(t.getLanguage().getLanguageCode()))
                                        .findFirst()
                                        .map(Text::getContent)
                                        .orElse(null)
                        ) + resource.getTags();

                this.sendMsgWithResource(chatId, mainBotEntity, pic.getResource(), message);
            }

            case NO_MATCH -> {
                BasicPic pic = basicPicService.findByType(BasicPicType.INVALID_HINT);

                message = pic.getResource().getTexts().stream()
                        .filter(t -> t.getLanguage().equals(language))
                        .findFirst()
                        .map(Text::getContent)
                        .orElseGet(() ->
                                resource.getTexts().stream()
                                        .filter(t -> "en".equalsIgnoreCase(t.getLanguage().getLanguageCode()))
                                        .findFirst()
                                        .map(Text::getContent)
                                        .orElse(null)
                        );

                this.sendMsgWithResource(chatId, mainBotEntity, pic.getResource(), message);
            }
        }
    }

    /**
     * 傳送如何使用hint的提示訊息
     */
    private void sendHowToUseHint(
            String chatId,
            Bot mainBotEntity,
            Language language
    ) {
        BasicPic pic = basicPicService.findByType(BasicPicType.HINT_MSG_INTRO);
        Resource resource = Optional.of(pic.getResource())
                .orElseThrow(() -> new ApiException(SysCode.RESOURCE_NOT_FOUNT));
        var text = resource.getTexts().stream()
                .filter(t -> t.getLanguage().equals(language))
                .findFirst()
                .map(Text::getContent)
                .orElse(null);
        this.sendMsgWithResource(chatId, mainBotEntity, resource, text);
    }

    /**
     * 傳送帶有資源的訊息
     */
    private void sendMsgWithResource(
            String chatId,
            Bot mainBotEntity,
            Resource resource,
            String text
    ) {
        switch (resource.getFileType()) {
            case PHOTO -> telegramBotClient.send(
                    SendPhoto.builder()
                            .chatId(chatId)
                            .photo(new InputFile(resource.getFileIdManageBot()))
                            .caption(text)
                            .build(),
                    mainBotEntity
            );
            case VIDEO -> telegramBotClient.send(
                    SendVideo.builder()
                            .chatId(chatId)
                            .video(new InputFile(resource.getFileIdManageBot()))
                            .caption(text)
                            .build(),
                    mainBotEntity
            );
            case GIF -> telegramBotClient.send(
                    SendAnimation.builder()
                            .chatId(chatId)
                            .animation(new InputFile(resource.getFileIdManageBot()))
                            .caption(text)
                            .build(),
                    mainBotEntity
            );
            default -> throw new IllegalArgumentException("Unsupported FileType: " + resource.getFileType());
        }
    }

    /**
     * 將 text取出並做處理
     */
    private String getStringInHint(Update update) {
        String text = update.getMessage().getText();
        return text.substring(getCommandName().length()).trim();
    }
}