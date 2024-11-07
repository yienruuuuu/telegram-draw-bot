package io.github.yienruuuuu.use_case.service.telegram;

import io.github.yienruuuuu.use_case.service.telegram.send_user_message.TelegramBotClient;
import io.github.yienruuuuu.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDice;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Comparator;

/**
 * @author Eric.Lee
 * Date: 2024/11/7
 */
@Component
@Slf4j
public class DrawBot implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramBotClient telegramBotClient;
    private final String TOKEN = "";

    public DrawBot(TelegramBotClient telegramBotClient) {
        this.telegramBotClient = telegramBotClient;
    }

    @Override
    public void consume(Update update) {
        //使用主BOT上傳文件並取得fileId
        JsonUtils.parseJsonAndPrintLog("收到響應", update);

        String fileId = update.getMessage().getPhoto().stream()
                .max(Comparator.comparingInt(PhotoSize::getFileSize))
                .orElseThrow(() -> new IllegalArgumentException("沒有照片"))
                .getFileId();
        File file = telegramBotClient.getFile(new GetFile(fileId), TOKEN);
        java.io.File downloadedFile = telegramBotClient.downloadFile(file, TOKEN);
        Message resMessage = telegramBotClient.send(new SendPhoto("1513052214", new InputFile(downloadedFile)), TOKEN);
        telegramBotClient.send( new SendDice("1513052214"), TOKEN);
        JsonUtils.parseJsonAndPrintLog("收到響應 resMessage", resMessage);
    }
}
