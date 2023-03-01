package az.code.telegrambotv2.services.interfaces;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface TourService {
    BotApiMethod<?> handleUpdate(Update update) throws TelegramApiException;
//    void sendOffer(Offer offer);
}
