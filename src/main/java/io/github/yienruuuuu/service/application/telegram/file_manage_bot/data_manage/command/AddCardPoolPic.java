package io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.command;

import io.github.yienruuuuu.bean.dto.AddCardPoolPicDto;
import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.entity.CardPool;
import io.github.yienruuuuu.bean.entity.Resource;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.DataManageCommand;
import io.github.yienruuuuu.service.business.*;
import io.github.yienruuuuu.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 新增卡池圖片指令處理器
 *
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Slf4j
@Component
public class AddCardPoolPic extends DataManageBaseCommand implements DataManageCommand {
    private final CardPoolService cardPoolService;

    public AddCardPoolPic(UserService userService, LanguageService languageService, TelegramBotClient telegramBotClient, AnnouncementService announcementService, ResourceService resourceService, CardPoolService cardPoolService) {
        super(userService, languageService, telegramBotClient, announcementService, resourceService);
        this.cardPoolService = cardPoolService;
    }

    @Override
    public void execute(Update update, Bot fileManageBot) {
        var userId = String.valueOf(update.getCallbackQuery().getFrom().getId());
        var chatId = String.valueOf(update.getCallbackQuery().getMessage().getChatId());
        //檢查操作權限
        checkUsersPermission(userId, chatId, fileManageBot);

        AddCardPoolPicDto dto =
                JsonUtils.parseJsonToTargetDto(update.getCallbackQuery().getData().substring(getCommandName().length()).trim(), AddCardPoolPicDto.class);
        CardPool cardPool = cardPoolService.findById(dto.getCardPoolId()).orElseThrow(() -> new IllegalArgumentException("CardPool not found."));


        if (StringUtils.isBlank(dto.getResourceUniqueId())) {
            this.listResourcesForChosen(dto, chatId, fileManageBot, cardPool);
        } else {
            Resource resource = resourceService.findById(Integer.parseInt(dto.getResourceUniqueId())).orElseThrow(() -> new IllegalArgumentException("Resource not found."));
            cardPool.setResource(resource);
            cardPoolService.save(cardPool);
            createCardPoolMessageAndSend(cardPool, chatId, fileManageBot);
        }
        CompletableFuture.runAsync(() -> telegramBotClient.send(AnswerCallbackQuery.builder().callbackQueryId(update.getCallbackQuery().getId()).build(), fileManageBot));
    }

    @Override
    public String getCommandName() {
        return "/add_pool_pic";
    }


    /**
     * 列出所有可用的資源供選擇
     */
    private void listResourcesForChosen(AddCardPoolPicDto dto, String chatId, Bot fileManageBot, CardPool cardPool) {
        // 提取分頁參數
        int pageNumber = dto.getPage() - 1;
        int pageSize = 1;

        Page<Resource> resourcePage = resourceService.findAllByPage(PageRequest.of(pageNumber, pageSize));

        if (resourcePage.isEmpty()) {
            telegramBotClient.send(SendMessage.builder()
                    .chatId(chatId)
                    .text("沒有更多資源可顯示。")
                    .build(), fileManageBot);
            return;
        }
        //傳送resource訊息
        resourcePage.getContent().forEach(resource -> createMediaMessageAndSendMedia(resource, chatId, fileManageBot, cardPool));

        // 附加分頁信息
        String pageInfo = String.format(" %d of %d", resourcePage.getNumber() + 1, resourcePage.getTotalPages());


        // 傳送當前頁數及下頁按鈕
        telegramBotClient.send(SendMessage.builder()
                .chatId(chatId)
                .text("頁數 :" + pageInfo)
                .replyMarkup(createInlineKeyBoard("下一頁", cardPool, resourcePage.getNumber() + 2, null))
                .build(), fileManageBot);
    }

    /**
     * 根據資源類型創建對應的媒體消息
     */
    private void createMediaMessageAndSendMedia(Resource resource, String chatId, Bot fileManageBot, CardPool cardPool) {
        String resourceId = String.valueOf(resource.getId());
        String text = "新增為卡池照片";
        switch (resource.getFileType()) {
            case PHOTO -> telegramBotClient.send(
                    SendPhoto.builder()
                            .chatId(chatId)
                            .photo(new InputFile(resource.getFileIdManageBot()))
                            .replyMarkup(createInlineKeyBoard(text, cardPool, 1, resourceId))
                            .build(),
                    fileManageBot
            );
            case VIDEO -> telegramBotClient.send(
                    SendVideo.builder()
                            .chatId(chatId)
                            .video(new InputFile(resource.getFileIdManageBot()))
                            .replyMarkup(createInlineKeyBoard(text, cardPool, 1, resourceId))
                            .build(),
                    fileManageBot
            );
            case GIF -> telegramBotClient.send(
                    SendAnimation.builder()
                            .chatId(chatId)
                            .animation(new InputFile(resource.getFileIdManageBot()))
                            .replyMarkup(createInlineKeyBoard(text, cardPool, 1, resourceId))
                            .build(),
                    fileManageBot
            );
            default -> throw new IllegalArgumentException("Unsupported FileType: " + resource.getFileType());
        }
    }

    /**
     * 創建並返回卡池功能按鈕行
     */
    private InlineKeyboardMarkup createInlineKeyBoard(String text, CardPool cardPool, Integer page, String resourceUniqueId) {
        InlineKeyboardButton addCardPoolPic =
                super.createInlineButton(text, "/add_pool_pic " + JsonUtils.parseJson(new AddCardPoolPicDto(cardPool.getId(), page, resourceUniqueId)));

        // 將所有列加入列表
        List<InlineKeyboardRow> rows = new ArrayList<>();
        rows.add(new InlineKeyboardRow(addCardPoolPic));

        // 返回四列的 InlineKeyboardMarkup
        return new InlineKeyboardMarkup(rows);
    }


    /**
     * 根據資源類型創建對應的媒體消息
     */
    private void createCardPoolMessageAndSend(CardPool cardPool, String chatId, Bot fileManageBot) {
        Resource cardPoolMedia = cardPool.getResource();
        String cardPoolDetail = String.join("\n",
                "活動id : " + cardPool.getId(),
                "開始於 : " + cardPool.getStartAt(),
                "結束於 : " + cardPool.getEndAt(),
                "啟用狀態 : " + cardPool.isOpen(),
                "資源id : " + (cardPool.getResource() == null ? "N/A" : cardPool.getResource().getId())
        );

        if (cardPoolMedia != null) {
            switch (cardPoolMedia.getFileType()) {
                case PHOTO -> telegramBotClient.send(
                        SendPhoto.builder()
                                .chatId(chatId)
                                .photo(new InputFile(cardPoolMedia.getFileIdManageBot()))
                                .caption(cardPoolDetail)
                                .build(),
                        fileManageBot
                );
                case VIDEO -> telegramBotClient.send(
                        SendVideo.builder()
                                .chatId(chatId)
                                .video(new InputFile(cardPoolMedia.getFileIdManageBot()))
                                .caption(cardPoolDetail)
                                .build(),
                        fileManageBot
                );
                case GIF -> telegramBotClient.send(
                        SendAnimation.builder()
                                .chatId(chatId)
                                .animation(new InputFile(cardPoolMedia.getFileIdManageBot()))
                                .caption(cardPoolDetail)
                                .build(),
                        fileManageBot
                );
                default -> throw new IllegalArgumentException("Unsupported FileType: " + cardPoolMedia.getFileType());
            }
        } else {
            telegramBotClient.send(
                    SendMessage.builder()
                            .chatId(chatId)
                            .text(cardPoolDetail)
                            .build(), fileManageBot);
        }
    }


}