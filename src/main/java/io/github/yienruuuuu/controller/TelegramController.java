package io.github.yienruuuuu.controller;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.enums.BotType;
import io.github.yienruuuuu.service.application.telegram.TelegramBotClient;
import io.github.yienruuuuu.service.business.BotService;
import io.github.yienruuuuu.utils.JsonUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.payments.RefundStarPayment;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updates.GetUpdates;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Eric.Lee
 * Date: 2024/11/8
 */
@Slf4j
@Tag(name = "Telegram controller")
@RestController
@RequestMapping("admin")
public class TelegramController {
    private final TelegramBotClient telegramBotClient;
    private final BotService botService;

    public TelegramController(TelegramBotClient telegramBotClient, BotService botService) {
        this.telegramBotClient = telegramBotClient;
        this.botService = botService;
    }

    @Operation(summary = "測試下載檔案")
    @PostMapping(value = "telegram/bot/file")
    public ResponseEntity<InputStreamResource> getVideo() throws FileNotFoundException {
        String fileId = "BAACAgUAAxkBAANzZzKtQwZwJDMu778Rsnu0qlXRNPIAAp8PAALpk5lVeS_ItzZZz402BA";
        Bot mainBotEntity = botService.findByBotType(BotType.MAIN);
        File file = telegramBotClient.getFile(new GetFile(fileId), mainBotEntity);
        java.io.File downloadedFile = telegramBotClient.downloadFile(file, mainBotEntity);  // 返回下載的文件

        if (downloadedFile == null) {
            // 文件下載失敗時的處理
            return ResponseEntity.status(500).body(null);
        }
        // 使用 InputStreamResource 來返回文件流
        InputStreamResource resource = new InputStreamResource(new FileInputStream(downloadedFile));
        String newFileName = "downloaded.mp4";

        // 設定回應頭，讓瀏覽器觸發下載
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + newFileName) // 設置文件名
                .contentType(MediaType.APPLICATION_OCTET_STREAM)  // 設置為通用的二進制文件類型
                .contentLength(downloadedFile.length())  // 設置文件大小
                .body(resource);  // 返回文件流
    }

    @Operation(summary = "測試傳送影片檔案")
    @PostMapping(value = "telegram/bot/send-video")
    public void sendVideo() {
        Bot mainBotEntity = botService.findByBotType(BotType.MAIN);

        SendVideo msg = SendVideo
                .builder()
                .chatId("1513052214")
                .video(new InputFile("BAACAgUAAxkBAANzZzKtQwZwJDMu778Rsnu0qlXRNPIAAp8PAALpk5lVeS_ItzZZz402BA"))
                .build();
        Message res = telegramBotClient.send(msg, mainBotEntity);
        System.out.println(JsonUtils.parseJson(res));
    }

    @Operation(summary = "測試傳送付費stars邀請")
    @GetMapping(value = "bots/sendPayStarsAsking")
    public void sendStarsAsking() {
        Bot mainBotEntity = botService.findByBotType(BotType.MAIN);

        SendInvoice sendInvoice = SendInvoice.builder()
                .chatId("1513052214")
                .title("購買數位商品")
                .description("用Telegram Stars支付以解鎖內容")
                .payload("唯一的識別ID")
                .currency("XTR")
                .protectContent(true)
                .price(new LabeledPrice("價格", 1))
                .providerToken("").build();


        Message mss = telegramBotClient.send(sendInvoice, mainBotEntity);
        JsonUtils.parseJsonAndPrintLog("收到響應", mss);
    }

    @Operation(summary = "測試傳送退款stars")
    @GetMapping(value = "bots/sendRefundStars/{telegramPaymentChargeId}")
    public void sendRefundStars(@PathVariable("telegramPaymentChargeId") String chargeId) {
        Bot mainBotEntity = botService.findByBotType(BotType.MAIN);
        RefundStarPayment msg = RefundStarPayment
                .builder()
                .telegramPaymentChargeId(chargeId)
                .userId(1513052214L)
                .build();

        Boolean mss = telegramBotClient.send(msg, mainBotEntity);
        JsonUtils.parseJsonAndPrintLog("收到響應", mss);
    }

    @Operation(summary = "測試傳送GetUpdates調整接收訊息")
    @GetMapping(value = "bots/sendGetUpdates")
    public void sendGetUpdates() {
        Bot channelBot = botService.findByBotType(BotType.CHANNEL);
        List<String> allowedUpdates = Arrays.asList("update_id","message","callback_query","channel_post","chat_member");
        GetUpdates msg = GetUpdates.builder()
                .allowedUpdates(allowedUpdates)
                .build();

        telegramBotClient.send(msg, channelBot);
    }
}
