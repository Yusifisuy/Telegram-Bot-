package az.code.telegrambotv2.services;

import az.code.telegrambotv2.bot.TourBot;
import az.code.telegrambotv2.configs.rabbit.CustomMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Slf4j
public class MessageListener {

    @Autowired
    TourBot tourBot;

    @RabbitListener(queues ="${sr.rabbit.queue.name}")
    public void handleNotification(String notification) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();



        CustomMessage customMessage = objectMapper.readValue(notification, CustomMessage.class);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(customMessage.getChatId());
        sendMessage.setText(customMessage.getAnswer());


//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setChatId("1279689927");
//        sendMessage.setText(notification);

        try {
            tourBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


}
