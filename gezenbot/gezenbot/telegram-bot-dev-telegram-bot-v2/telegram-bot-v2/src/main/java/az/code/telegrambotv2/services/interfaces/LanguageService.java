package az.code.telegrambotv2.services.interfaces;

import az.code.telegrambotv2.models.entities.Language;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LanguageService {

    List<Language> getAll();
    Language getByName(String language);
    int getIdByName(String langName);
}
