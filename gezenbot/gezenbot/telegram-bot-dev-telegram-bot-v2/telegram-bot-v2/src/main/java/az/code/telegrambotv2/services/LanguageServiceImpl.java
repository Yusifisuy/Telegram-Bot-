package az.code.telegrambotv2.services;

import az.code.telegrambotv2.models.entities.Language;
import az.code.telegrambotv2.repositories.LanguageRepository;
import az.code.telegrambotv2.services.interfaces.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageServiceImpl implements LanguageService {

    private LanguageRepository repository;

    public LanguageServiceImpl(LanguageRepository repository){
        this.repository = repository;
    }

    @Override
    public List<Language> getAll() {
        return repository.findAll();
    }

    @Override
    public Language getByName(String language) {
        return repository.findLanguageByName(language.toLowerCase());
    }

    @Override
    public int getIdByName(String langName) {
        return repository.getIdByName(langName);
    }
}
