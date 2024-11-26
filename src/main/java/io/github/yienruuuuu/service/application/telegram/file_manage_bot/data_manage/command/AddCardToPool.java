package io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.command;

import io.github.yienruuuuu.bean.dto.AddCardToPoolDto;
import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.entity.Card;
import io.github.yienruuuuu.bean.entity.CardPool;
import io.github.yienruuuuu.bean.entity.Resource;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.application.telegram.file_manage_bot.data_manage.DataManageCommand;
import io.github.yienruuuuu.service.business.*;
import io.github.yienruuuuu.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
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
 * 新增卡牌到卡池
 *
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Slf4j
@Component
public class AddCardToPool extends DataManageBaseCommand implements DataManageCommand {
    private final CardPoolService cardPoolService;
    private final CardService cardService;

    public AddCardToPool(UserService userService, LanguageService languageService, TelegramBotClient telegramBotClient, AnnouncementService announcementService, ResourceService resourceService, CardPoolService cardPoolService, CardService cardService) {
        super(userService, languageService, telegramBotClient, announcementService, resourceService);
        this.cardPoolService = cardPoolService;
        this.cardService = cardService;
    }

    @Override
    public void execute(Update update, Bot fileManageBot) {
        var userId = String.valueOf(update.getCallbackQuery().getFrom().getId());
        var chatId = String.valueOf(update.getCallbackQuery().getMessage().getChatId());
        var messageId = update.getCallbackQuery().getMessage().getMessageId();
        var callbackQueryId = update.getCallbackQuery().getId();
        //檢查操作權限
        checkUsersPermission(userId, chatId, fileManageBot);

        //取出參數
        AddCardToPoolDto dto =
                JsonUtils.parseJsonToTargetDto(update.getCallbackQuery().getData().substring(getCommandName().length()).trim(), AddCardToPoolDto.class);
        //取得卡池
        CardPool cardPool = cardPoolService.findById(dto.getCpId()).orElseThrow(() -> new IllegalArgumentException("CardPool not found"));

        if (dto.getRId() == null) {
            // 若沒有指定 resource id，則列出 resource 並分頁傳送
            listResourcesByPage(cardPool, dto, chatId, fileManageBot);
        } else {
            // 若有指定 resource id，則儲存為卡牌
            addResourceAsCard(dto, cardPool, chatId, messageId, fileManageBot);
        }
        CompletableFuture.runAsync(() -> telegramBotClient.send(AnswerCallbackQuery.builder().callbackQueryId(callbackQueryId).build(), fileManageBot));
    }

    @Override
    public String getCommandName() {
        return "/add_card_to_pool";
    }

    /**
     * 添加資源為卡牌
     */
    private void addResourceAsCard(AddCardToPoolDto dto, CardPool cardPool, String chatId, Integer messageId, Bot fileManageBot) {
        Resource res = resourceService.findById(dto.getRId()).orElseThrow(() -> new IllegalArgumentException("Resource not found"));
        Card newCard = Card.builder()
                .cardPool(cardPool)
                .resource(res)
                .dropRate(res.getRarityType().getDefaultDropRate())
                .build();
        Card card = cardService.save(newCard);

        EditMessageCaption editMessage = EditMessageCaption.builder()
                .chatId(chatId)
                .messageId(messageId)
                .caption("已儲存卡牌: card id= " + card.getId())
                .replyMarkup(null)
                .build();
        telegramBotClient.send(
                editMessage,
                fileManageBot
        );
    }


    /**
     * 分頁列出資源
     */
    private void listResourcesByPage(CardPool cardPool, AddCardToPoolDto dto, String chatId, Bot fileManageBot) {
        // 提取卡池中的 resourceId 列表
        List<Integer> excludedResourceIds = new ArrayList<>();
        if (cardPool.getCards() != null && !cardPool.getCards().isEmpty()) {
            excludedResourceIds = cardPool.getCards().stream()
                    .map(card -> card.getResource().getId())
                    .toList();
        }

        // 提取分頁參數，默認為第 1 頁
        int pageNumber = dto.getPg() - 1; // Pageable 的頁碼從 0 開始
        int pageSize = 10;
        Page<Resource> resourcePage = resourceService.findAllByPageExcludingIds(PageRequest.of(pageNumber, pageSize), excludedResourceIds);

        if (resourcePage.isEmpty()) {
            telegramBotClient.send(SendMessage.builder()
                    .chatId(chatId)
                    .text("No more resource to display.")
                    .build(), fileManageBot);
            return;
        }
        //傳送resource列表
        resourcePage.getContent().forEach(resource -> createMediaMessageAndSendMedia(cardPool, resource, chatId, fileManageBot));

        // 附加分頁信息
        String pageInfo = String.format(" %d of %d", resourcePage.getNumber() + 1, resourcePage.getTotalPages());

        // 傳送當前頁數及下頁按鈕
        telegramBotClient.send(SendMessage.builder()
                .chatId(chatId)
                .text("Page :" + pageInfo)
                .replyMarkup(createPaginationKeyboard(cardPool, resourcePage.getNumber() + 2))
                .build(), fileManageBot);
    }


    /**
     * 根據資源類型創建對應的媒體消息
     */
    private void createMediaMessageAndSendMedia(CardPool cardPool, Resource resource, String chatId, Bot fileManageBot) {
        var inputFile = new InputFile(resource.getFileIdManageBot());
        var replyMarkup = createResourceKeyboard(cardPool, resource);
        var captionText = resource.getUniqueId();

        switch (resource.getFileType()) {
            case PHOTO -> telegramBotClient.send(
                    SendPhoto.builder()
                            .chatId(chatId)
                            .photo(inputFile)
                            .caption(captionText)
                            .replyMarkup(replyMarkup)
                            .build(),
                    fileManageBot
            );
            case VIDEO -> telegramBotClient.send(
                    SendVideo.builder()
                            .chatId(chatId)
                            .video(inputFile)
                            .caption(captionText)
                            .replyMarkup(replyMarkup)
                            .build(),
                    fileManageBot
            );
            case GIF -> telegramBotClient.send(
                    SendAnimation.builder()
                            .chatId(chatId)
                            .animation(inputFile)
                            .caption(captionText)
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
    private InlineKeyboardMarkup createResourceKeyboard(CardPool cardPool, Resource resource) {
        InlineKeyboardButton addCardPoolPic =
                super.createInlineButton("加為卡牌", "/add_card_to_pool " + JsonUtils.parseJson(new AddCardToPoolDto(cardPool.getId(), 1, resource.getId())));

        // 將所有列加入列表
        List<InlineKeyboardRow> rows = new ArrayList<>();
        rows.add(new InlineKeyboardRow(addCardPoolPic));

        // 返回 InlineKeyboardMarkup
        return new InlineKeyboardMarkup(rows);
    }

    /**
     * 創建並返回功能按鈕行
     */
    private InlineKeyboardMarkup createPaginationKeyboard(CardPool cardPool, Integer page) {
        InlineKeyboardButton addCardPoolPic =
                super.createInlineButton("下一頁", "/add_card_to_pool " + JsonUtils.parseJson(new AddCardToPoolDto(cardPool.getId(), page - 1, null)));

        // 將所有列加入列表
        List<InlineKeyboardRow> rows = new ArrayList<>();
        rows.add(new InlineKeyboardRow(addCardPoolPic));

        // 返回 InlineKeyboardMarkup
        return new InlineKeyboardMarkup(rows);
    }
}