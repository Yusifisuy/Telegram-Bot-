package az.code.telegrambotv2.bot;

import az.code.telegrambotv2.configs.BotConfig;
import az.code.telegrambotv2.services.interfaces.TourService;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourBot extends TelegramWebhookBot {

    @Autowired
    BotConfig botConfig;

    @Autowired
    TourService service;



//    public TourBot(TourService service){
//        this.service = service;
//    }

    @SneakyThrows
    @Override
    public BotApiMethod onWebhookUpdateReceived(Update update) {
        final BotApiMethod<?> replyMessageToUser = service.handleUpdate(update);
        return replyMessageToUser;
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public String getBotPath() {
        return botConfig.getPath();
    }

    public BotConfig getBotConfig() {
        return botConfig;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    public void setBotConfig(BotConfig botConfig) {
        this.botConfig = botConfig;
    }

    public void setService(TourService service) {
        this.service = service;
    }
}
