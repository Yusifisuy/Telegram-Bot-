package az.code.telegrambotv2.repositories;

import az.code.telegrambotv2.models.entities.QuestionLocale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuestionLocaleRepository extends JpaRepository<QuestionLocale,Long> {

        @Query("SELECT q FROM QuestionLocale q " +
                "WHERE q.language.id = ?1")
        List<QuestionLocale> findAllByLanguage_Id(int langId);

        @Query("select q FROM QuestionLocale q " +
                "WHERE q.language.id = ?1 AND q.question.id = ?2")
        QuestionLocale findQuestionLocalesByLanguageId(String lang, long questionId);

        @Query("select q.text FROM QuestionLocale q " +
                "WHERE q.language.id = ?1 AND q.question.id = ?2")
        String getTranslate(String lang, long id);

//        @Query("select q FROM QuestionLocale q " +
//                "WHERE q.language.id = ?1 AND q.question.id = ?2")
//        QuestionLocale findQuestionLocalesByLanguageId(int langId, long questionId);
}
