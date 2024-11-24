package io.github.yienruuuuu.service.application.telegram.main_bot;

import io.github.yienruuuuu.bean.entity.Bot;
import io.github.yienruuuuu.bean.enums.BotType;
import io.github.yienruuuuu.config.AppConfig;
import io.github.yienruuuuu.service.application.telegram.main_bot.dispatcher.CallbackDispatcher;
import io.github.yienruuuuu.service.application.telegram.main_bot.dispatcher.CheckOutDispatcher;
import io.github.yienruuuuu.service.application.telegram.main_bot.dispatcher.CommandDispatcher;
import io.github.yienruuuuu.service.business.BotService;
import io.github.yienruuuuu.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author Eric.Lee Date: 2024/10/16
 */

@Component
@Slf4j
public class MainBotConsumer implements LongPollingSingleThreadUpdateConsumer {
    private final BotService botService;
    private final AppConfig appConfig;
    //Dispatcher
    private final CommandDispatcher commandDispatcher;
    private final CallbackDispatcher callbackDispatcher;
    private final CheckOutDispatcher checkOutDispatcher;
    //handler
    private final ChannelPostHandler channelPostHandler;


    @Autowired
    public MainBotConsumer(BotService botService, AppConfig appConfig, CommandDispatcher commandDispatcher, CallbackDispatcher callbackDispatcher, CheckOutDispatcher checkOutDispatcher, ChannelPostHandler channelPostHandler) {
        this.botService = botService;
        this.appConfig = appConfig;
        this.commandDispatcher = commandDispatcher;
        this.callbackDispatcher = callbackDispatcher;
        this.checkOutDispatcher = checkOutDispatcher;
        this.channelPostHandler = channelPostHandler;
    }

    @Override
    public void consume(Update update) {
        // 取得當前時間戳（秒）
        long currentTimestamp = (System.currentTimeMillis() / 1000) - 10;
        System.out.println(currentTimestamp);
        JsonUtils.parseJsonAndPrintLog("MAIN BOT CONSUMER收到Update訊息", update);
        Bot mainBotEntity = botService.findByBotType(BotType.MAIN);

        if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getDate() > currentTimestamp) {
            commandDispatcher.dispatch(update, mainBotEntity);
        } else if (update.hasMessage() && update.getMessage().hasDice() && update.getMessage().getDate() > currentTimestamp) {
            //色子遊戲
            update.getMessage().setText("/dice");
            commandDispatcher.dispatch(update, mainBotEntity);
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getMessage().getDate() > currentTimestamp) {
            callbackDispatcher.dispatch(update, mainBotEntity);
        } else if (update.hasChannelPost() && update.getChannelPost().getChatId().toString().equals(appConfig.getBotCommunicatorChatId())) {
            channelPostHandler.handleChannelPost(update);
        } else if (update.hasPreCheckoutQuery()) { /*預支付訊息處理*/
            checkOutDispatcher.dispatch(update, mainBotEntity);
        } else if (update.hasMessage() && update.getMessage().hasSuccessfulPayment()) { /*支付訊息處理*/
            checkOutDispatcher.dispatch(update, mainBotEntity);
        } else {
            log.warn("MAIN BOT CONSUMER收到不支援的更新類型");
        }
    }
}

