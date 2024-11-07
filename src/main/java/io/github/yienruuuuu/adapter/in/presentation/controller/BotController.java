package io.github.yienruuuuu.adapter.in.presentation.controller;

import io.github.yienruuuuu.use_case.service.telegram.register_bot.RegisterBotService;
import io.github.yienruuuuu.use_case.service.telegram.send_user_message.TelegramBotClient;
import io.github.yienruuuuu.utils.JsonUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.message.Message;

/**
 * @author Eric.Lee
 * Date: 2024/11/6
 */
@Tag(name = "BOT Controller", description = "BOT控制調適用")
@Slf4j
@RestController
@RequestMapping("/telegram-bot")
public class BotController {
    private final RegisterBotService registerBotService;
    private final TelegramBotClient telegramBotClient;
    private final String TOKEN = "";


    public BotController(RegisterBotService registerBotService, TelegramBotClient telegramBotClient) {
        this.registerBotService = registerBotService;
        this.telegramBotClient = telegramBotClient;
    }


    @PostMapping("/send-photo")
    public ResponseEntity<?> sendPhoto(@RequestBody String fileId) {

        SendPhoto msg = SendPhoto
                .builder()
                .chatId("1513052214")
                .photo(new InputFile(fileId))
                .caption("請選擇您的每日幸運卡牌(可能含有色情圖片，請確認是否已滿18歲)")
                .build();

        Message mss = telegramBotClient.send(msg, TOKEN);
        JsonUtils.parseJsonAndPrintLog("收到響應", mss);
        return ResponseEntity.ok().build();
    }

    @PostConstruct
    public void registerBot() {
        registerBotService.registerBot(TOKEN);
    }
}
