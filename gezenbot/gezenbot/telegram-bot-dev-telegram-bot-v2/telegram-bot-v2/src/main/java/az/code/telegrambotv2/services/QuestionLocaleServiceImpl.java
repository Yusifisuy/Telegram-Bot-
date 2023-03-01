package az.code.telegrambotv2.services;

import az.code.telegrambotv2.models.entities.Question;
import az.code.telegrambotv2.models.entities.QuestionLocale;
import az.code.telegrambotv2.repositories.QuestionLocaleRepository;
import az.code.telegrambotv2.services.interfaces.QuestionLocaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionLocaleServiceImpl implements QuestionLocaleService {
    @Autowired

    private QuestionLocaleRepository questionLocaleRepository;

    @Override
    public QuestionLocale nextQuestionId(String lang, long id) {
        return questionLocaleRepository.findQuestionLocalesByLanguageId(lang, ++id);
    }

    @Override
    public String getTranslate(String lang, long id) {
        return questionLocaleRepository.getTranslate(lang, id);
    }

}
