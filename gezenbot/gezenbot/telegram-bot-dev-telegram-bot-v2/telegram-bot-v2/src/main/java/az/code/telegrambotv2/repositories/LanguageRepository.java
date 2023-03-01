package az.code.telegrambotv2.repositories;

import az.code.telegrambotv2.models.entities.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LanguageRepository extends JpaRepository<Language, Integer> {

    @Query("SELECT l FROM Language l")
    List<Language> findAll();


    @Query("SELECT l FROM Language l WHERE lower(l.name) = ?1")
    Language findLanguageByName(String langId);

    @Query("SELECT l.id FROM Language l WHERE lower(l.name) = ?1")
    int getIdByName(String langName);
}
