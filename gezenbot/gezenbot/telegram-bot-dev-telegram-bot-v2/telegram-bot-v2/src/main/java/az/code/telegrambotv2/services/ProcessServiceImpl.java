package az.code.telegrambotv2.services;

import az.code.telegrambotv2.models.entities.Language;
import az.code.telegrambotv2.models.entities.Question;
import az.code.telegrambotv2.models.redis.Session;
import az.code.telegrambotv2.repositories.LanguageRepository;
import az.code.telegrambotv2.repositories.QuestionLocaleRepository;
import az.code.telegrambotv2.repositories.redis.SessionRepository;
import az.code.telegrambotv2.services.interfaces.LanguageService;
import az.code.telegrambotv2.services.interfaces.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

@Service
public class ProcessServiceImpl implements ProcessService {

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    QuestionLocaleServiceImpl questionLocaleService;

    @Autowired
    LanguageService languageService;

    @Override
    public void createSession(Message message) {
        Session session = Session.builder()
                .uuid(UUID.randomUUID().toString())
                .chatId(message.getChatId()).build();


        Timestamp timestamp = new Timestamp(new Date().getTime());
//        session.setDate(timestamp);

        session.setDate(LocalDate.now());
        sessionRepository.save(session);
    }

    @Override
    public void setCurrentQuestionId(long chatId, long questionId) {
        Session session = sessionRepository.find(chatId);
        session.setCurrentQuestionId(questionId);
        //TODO fix bug with set date (redis)
//        session.setDate(LocalDate.now());
//        System.out.println("CHECK TIME " + session.getDate());
        sessionRepository.save(session);
    }

    @Override
    public long getCurrentQuestionId(long chatId) {
        if (sessionRepository.find(chatId) == null) {
            return 0;
        }
        return sessionRepository.find(chatId).getCurrentQuestionId();
    }

    public Session getSession(long chatId) {
        if (sessionRepository.find(chatId) == null) {
            return null;
        }

        return sessionRepository.find(chatId);
    }

//    public Map<String, String> getAnswers(long chatId) {
//        if (sessionRepository.find(chatId) == null) {
//            return null;
//        }
//        return sessionRepository.
//    }

    @Override
    public void saveUserAnswers(long chatId, String key, String answer) {
        Session session = sessionRepository.find(chatId);
        Map<String,String> userAnswers = session.getAnswers();
        if(userAnswers == null){
            userAnswers = new HashMap<>();
        }
        userAnswers.put(key,answer);
        session.setAnswers(userAnswers);
        sessionRepository.save(session);
    }

    @Override
    public boolean hasActiveSession(long chatId) {
        return sessionRepository.find(chatId) != null;
    }

    @Override
    public void disableActivePoll(long chatId) {
        //TODO active sessionu disable etmek
//        sessionRepository.delete(chatId);
//        selectionRepository.delete(clientId);
//        List<Request> requests = requestService.findByClientId(clientId);
//        requests.stream().filter(r -> r.getIsActive())
//                .forEach(r -> {
//                    r.setIsActive(false);
//                    requestService.save(r);
//                });

    }

    @Transactional
    @Override
    public void stopActivePoll(long clientId){
        sessionRepository.delete(clientId);
//        selectionRepository.delete(clientId);
//        List<Request> requests = requestService.findByClientId(clientId);
//        requests.stream().filter(r -> r.getIsActive() == null || r.getIsActive())
//                .forEach(r -> {
//                    r.setIsActive(false);
//                    requestService.save(r);
//                    rabbitmqService.sendToStopQueue(r.getUuid());
//                });
    }

    @Override
    public void setSelectedLanguage(long chatId, String language) {
        Language lang = languageService.getByName(language);

        String selectedLang = (lang.getName() == null) ? null : language;

        Session session = sessionRepository.find(chatId);
        session.setUserLanguage(selectedLang);
        sessionRepository.save(session);
    }

    @Override
    public String getSelectedLanguage(long chatId) {
        if (sessionRepository.find(chatId) == null){
            return null;
        }
        return sessionRepository.find(chatId).getUserLanguage();
    }

    @Override
    public void setCalendarMonth(long chatId, LocalDate date) {

    }

//    @Override
//    public void setCalendarMonth(long chatId, Timestamp date) {
//        Session session = sessionRepository.find(chatId);
//
//        session.setDate(date);
//        sessionRepository.save(session);
//    }


}


