package az.code.telegrambotv2.repositories;

import az.code.telegrambotv2.models.entities.Language;
import az.code.telegrambotv2.models.entities.Question;
import az.code.telegrambotv2.models.entities.QuestionLocale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

    @Query("SELECT q from Question q " +
            "INNER JOIN QuestionLocale u " +
            "on q.id  = u.question.id " +
            "WHERE u.language.name = ?1 AND u.question.id = ?2")
    Question findByLanguageAndId(String lang, long questionId);

    @Query("SELECT q from Question q " +
            "INNER JOIN QuestionLocale u " +
            "on q.id  = u.question.id " +
            "WHERE u.language.name = ?1 AND u.question.id = 1")
    Question findFirstQuestion(String lang);





    @Query("SELECT q from Question q " +
            "INNER JOIN QuestionLocale u " +
            "on q.id  = u.question.id " +
            "WHERE u.language.name = ?1 AND q.nextQuestionId = -1")
    Question findLastQuestion(String lang);

    @Query("SELECT q from Question q " +
            "INNER JOIN QuestionLocale u " +
            "on q.id  = u.question.id " +
            "WHERE q.id = ?1")
    Question findById(long questionId);
}
