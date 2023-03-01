package az.code.telegrambotv2.services;

import az.code.telegrambotv2.models.entities.Question;
import az.code.telegrambotv2.models.entities.QuestionLocale;
import az.code.telegrambotv2.repositories.QuestionRepository;
import az.code.telegrambotv2.services.interfaces.QuestionLocaleService;
import az.code.telegrambotv2.services.interfaces.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public Question findByLanguageAndId(String lang, long id) {
        return questionRepository.findByLanguageAndId(lang, id);
    }


    @Override
    public Question findFirstQuestion(String lang) {
        return questionRepository.findFirstQuestion(lang);
    }

    @Override
    public Question findLastQuestion(String lang) {
        return questionRepository.findLastQuestion(lang);
    }


    @Override
    public Question findById(long questionId) {
        return questionRepository.findById(questionId);
    }
}
