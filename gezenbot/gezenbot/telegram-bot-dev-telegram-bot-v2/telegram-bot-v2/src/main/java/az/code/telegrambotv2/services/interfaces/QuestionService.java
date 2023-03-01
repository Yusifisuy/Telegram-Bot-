package az.code.telegrambotv2.services.interfaces;

import az.code.telegrambotv2.models.entities.Question;

import java.util.List;

public interface QuestionService {

    Question findByLanguageAndId(String lang, long questionId);

    Question findFirstQuestion(String lang);

    Question findLastQuestion(String lang);

    Question findById(long questionId);


}
