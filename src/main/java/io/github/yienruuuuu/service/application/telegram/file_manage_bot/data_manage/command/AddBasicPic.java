package io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.command;

import io.github.yienruuuuu.bean.dto.AddBasicPicDto;
import io.github.yienruuuuu.bean.entity.BasicPic;
import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.entity.Resource;
import io.github.yienruuuuu.bean.enums.BasicPicType;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.DataManageCommand;
import io.github.yienruuuuu.service.business.*;
import io.github.yienruuuuu.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageCaption;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 為基礎圖片指定資源
 *
 * @author Eric.Lee
 * Date: 2025/08/21
 */
@Slf4j
@Component
public class AddBasicPic extends DataManageBaseCommand implements DataManageCommand {
    private final BasicPicService basicPicService;

    public AddBasicPic(
            UserService userService,
            LanguageService languageService,
            TelegramBotClient telegramBotClient,
            AnnouncementService announcementService,
            ResourceService resourceService,
            BasicPicService basicPicService
    ) {
        super(userService, languageService, telegramBotClient, announcementService, resourceService);
        this.basicPicService = basicPicService;
    }

    @Transactional
    @Override
    public void execute(Update update, Bot fileManageBot) {
        var userId = String.valueOf(update.getCallbackQuery().getFrom().getId());
        var chatId = String.valueOf(update.getCallbackQuery().getMessage().getChatId());
        var messageId = update.getCallbackQuery().getMessage().getMessageId();
        var callbackQueryId = update.getCallbackQuery().getId();
        CompletableFuture.runAsync(() -> telegramBotClient.send(AnswerCallbackQuery.builder().callbackQueryId(callbackQueryId).build(), fileManageBot));
        //檢查操作權限
        checkUsersPermission(userId, chatId, fileManageBot);

        //取出參數
        AddBasicPicDto dto =
                JsonUtils.parseJsonToTargetDto(update.getCallbackQuery().getData().substring(getCommandName().length()).trim(), AddBasicPicDto.class);


        if (dto.getRId() == null) {
            // 若沒有指定 resource id，則列出 resource 並分頁傳送
            this.listResourcesByPage(dto.getT(), dto, chatId, fileManageBot);
        } else {
            // 若有指定 resource id，則儲存為卡牌
            this.addResourceAsCard(dto, dto.getT(), chatId, messageId, fileManageBot);
        }
        CompletableFuture.runAsync(() -> telegramBotClient.send(AnswerCallbackQuery.builder().callbackQueryId(callbackQueryId).build(), fileManageBot));
    }

    @Override
    public String getCommandName() {
        return "/add_basic_pic";
    }

    /**
     * 添加資源為卡牌
     */
    private void addResourceAsCard(
            AddBasicPicDto dto,
            BasicPicType type,
            String chatId,
            Integer messageId,
            Bot fileManageBot
    ) {
        Resource res = resourceService.findById(dto.getRId())
                .orElseThrow(() -> new IllegalArgumentException("Resource not found"));
        super.markAsUsed(res);

        BasicPic basicPic = basicPicService.findByType(type);
        if (basicPic == null) {
            basicPic = BasicPic.builder()
                    .type(type)
                    .resource(res)
                    .build();
        } else {
            super.markAsUnused(basicPic.getResource());
            basicPic.setResource(res);
        }

        basicPicService.save(basicPic);
        telegramBotClient.send(
                EditMessageCaption.builder()
                        .chatId(chatId)
                        .messageId(messageId)
                        .caption("已儲存")
                        .replyMarkup(null)
                        .build(),
                fileManageBot
        );
    }


    /**
     * 分頁列出資源
     */
    private void listResourcesByPage(BasicPicType picType, AddBasicPicDto dto, String chatId, Bot fileManageBot) {

        // 提取分頁參數，默認為第 1 頁
        int pageNumber = dto.getPg() - 1; // Pageable 的頁碼從 0 開始
        int pageSize = 10;
        Page<Resource> resourcePage = resourceService.findAllByInUsedAndPage(PageRequest.of(pageNumber, pageSize), false);

        if (resourcePage.isEmpty()) {
            telegramBotClient.send(SendMessage.builder()
                    .chatId(chatId)
                    .text("No more resource to display.")
                    .build(), fileManageBot);
            return;
        }
        //傳送resource列表
        resourcePage.getContent().forEach(
                resource -> this.createBasicPicMessageAndSend(picType, resource, chatId, fileManageBot)
        );

        // 附加分頁信息
        String pageInfo = String.format(" %d of %d", resourcePage.getNumber() + 1, resourcePage.getTotalPages());

        // 傳送當前頁數及下頁按鈕
        telegramBotClient.send(
                SendMessage.builder()
                        .chatId(chatId)
                        .text("Page :" + pageInfo)
                        .replyMarkup(createPaginationKeyboard(picType, resourcePage.getNumber() + 2))
                        .build(),
                fileManageBot
        );
    }


    /**
     * 根據資源類型創建對應的媒體消息
     */
    private void createBasicPicMessageAndSend(BasicPicType picType, Resource resource, String chatId, Bot fileManageBot) {
        var inputFile = new InputFile(resource.getFileIdManageBot());
        var replyMarkup = createResourceKeyboard(picType, resource);

        switch (resource.getFileType()) {
            case PHOTO -> telegramBotClient.send(
                    SendPhoto.builder()
                            .chatId(chatId)
                            .photo(inputFile)
                            .replyMarkup(replyMarkup)
                            .build(),
                    fileManageBot
            );
            case VIDEO -> telegramBotClient.send(
                    SendVideo.builder()
                            .chatId(chatId)
                            .video(inputFile)
                            .replyMarkup(replyMarkup)
                            .build(),
                    fileManageBot
            );
            case GIF -> telegramBotClient.send(
                    SendAnimation.builder()
                            .chatId(chatId)
                            .animation(inputFile)
                            .replyMarkup(replyMarkup)
                            .build(),
                    fileManageBot
            );
            default -> throw new IllegalArgumentException("Unsupported FileType: " + resource.getFileType());
        }
    }

    /**
     * 創建並返回功能按鈕行
     */
    private InlineKeyboardMarkup createResourceKeyboard(BasicPicType type, Resource resource) {
        InlineKeyboardButton addCardPoolPic =
                super.createInlineButton(
                        "指定為基礎資源",
                        "/add_basic_pic " + JsonUtils.parseJson(
                                new AddBasicPicDto(type, 1, resource.getId())
                        )
                );

        // 將所有列加入列表
        List<InlineKeyboardRow> rows = new ArrayList<>();
        rows.add(new InlineKeyboardRow(addCardPoolPic));

        // 返回 InlineKeyboardMarkup
        return new InlineKeyboardMarkup(rows);
    }

    /**
     * 創建並返回功能按鈕行
     */
    private InlineKeyboardMarkup createPaginationKeyboard(BasicPicType type, Integer page) {
        InlineKeyboardButton addCardPoolPic =
                super.createInlineButton(
                        "下一頁",
                        "/add_basic_pic " + JsonUtils.parseJson(
                                new AddBasicPicDto(type, page - 1, null)
                        )
                );

        // 將所有列加入列表
        List<InlineKeyboardRow> rows = new ArrayList<>();
        rows.add(new InlineKeyboardRow(addCardPoolPic));

        // 返回 InlineKeyboardMarkup
        return new InlineKeyboardMarkup(rows);
    }

}