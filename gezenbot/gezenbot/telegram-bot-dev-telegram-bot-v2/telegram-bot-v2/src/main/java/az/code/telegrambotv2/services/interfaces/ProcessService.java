package az.code.telegrambotv2.services.interfaces;

import az.code.telegrambotv2.models.entities.Question;
import az.code.telegrambotv2.models.redis.Session;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDate;

public interface ProcessService {

    void createSession(Message message);

     void setCurrentQuestionId(long clientId, long questionId);

    long getCurrentQuestionId(long chatId);

    void saveUserAnswers(long chatId, String key, String answer);

    boolean hasActiveSession(long chatId);

    void disableActivePoll(long chatId);

    void stopActivePoll(long clientId);

    Session getSession(long chatId);

   // void expireActivePoll(Request request)

    void setSelectedLanguage(long chatId, String language);

    String getSelectedLanguage(long chatId);

    void setCalendarMonth(long chatId, LocalDate date);

}
