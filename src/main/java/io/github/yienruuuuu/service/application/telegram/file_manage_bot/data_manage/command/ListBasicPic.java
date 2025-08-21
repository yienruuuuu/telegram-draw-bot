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
import org.springframework.stereotype.Component;
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
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 新增卡池圖片指令處理器
 *
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Slf4j
@Component
public class ListBasicPic extends DataManageBaseCommand implements DataManageCommand {
    private final BasicPicService basicPicService;

    public ListBasicPic(
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

    @Override
    public void execute(Update update, Bot fileManageBot) {
        var userId = String.valueOf(update.getMessage().getFrom().getId());
        var chatId = String.valueOf(update.getMessage().getChatId());
        //檢查操作權限
        checkUsersPermission(userId, chatId, fileManageBot);

        // 取得所有 BasicPic
        this.createBasicPicMessageAndSend(chatId, fileManageBot);

    }


    @Override
    public String getCommandName() {
        return "/list_basic_pic";
    }


    /**
     * 根據資源類型創建對應的媒體消息
     */
    private void createBasicPicMessageAndSend(String chatId, Bot fileManageBot) {
        Map<BasicPicType, BasicPic> picMap = basicPicService.findAll().stream()
                .collect(Collectors.toMap(BasicPic::getType, pic -> pic));

        for (BasicPicType type : BasicPicType.values()) {
            BasicPic basicPic = picMap.get(type);
            String caption = String.format("類型: %s", type);

            if (basicPic != null && basicPic.getResource() != null) {
                Resource resource = basicPic.getResource();


                switch (resource.getFileType()) {
                    case PHOTO -> telegramBotClient.send(
                            SendPhoto.builder()
                                    .chatId(chatId)
                                    .photo(new InputFile(resource.getFileIdManageBot()))
                                    .caption(caption)
                                    .replyMarkup(createInlineKeyBoard(type))
                                    .build(),
                            fileManageBot
                    );
                    case VIDEO -> telegramBotClient.send(
                            SendVideo.builder()
                                    .chatId(chatId)
                                    .video(new InputFile(resource.getFileIdManageBot()))
                                    .caption(caption)
                                    .replyMarkup(createInlineKeyBoard(type))
                                    .build(),
                            fileManageBot
                    );
                    case GIF -> telegramBotClient.send(
                            SendAnimation.builder()
                                    .chatId(chatId)
                                    .animation(new InputFile(resource.getFileIdManageBot()))
                                    .caption(caption)
                                    .replyMarkup(createInlineKeyBoard(type))
                                    .build(),
                            fileManageBot
                    );
                    default -> throw new IllegalArgumentException("Unsupported FileType: " + resource.getFileType());
                }
            } else {
                telegramBotClient.send(
                        SendMessage.builder()
                                .chatId(chatId)
                                .text(caption)
                                .replyMarkup(createInlineKeyBoard(type))
                                .build(),
                        fileManageBot
                );
            }
        }
    }


    /**
     * 創建並返回卡池功能按鈕行
     */
    private InlineKeyboardMarkup createInlineKeyBoard(BasicPicType type) {
        InlineKeyboardButton addCardPoolPic =
                super.createInlineButton(
                        "指定資源",
                        "/add_basic_pic " + JsonUtils.parseJson(
                                new AddBasicPicDto(type, 1, null)
                        )
                );

        // 將所有列加入列表
        List<InlineKeyboardRow> rows = new ArrayList<>();
        rows.add(new InlineKeyboardRow(addCardPoolPic));

        // 返回 InlineKeyboardMarkup
        return new InlineKeyboardMarkup(rows);
    }
}