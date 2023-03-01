package az.code.telegrambotv2.services.interfaces;

import az.code.telegrambotv2.models.entities.QuestionLocale;

public interface QuestionLocaleService {

    QuestionLocale nextQuestionId(String lang, long id);
    String getTranslate(String lang, long id);

}
