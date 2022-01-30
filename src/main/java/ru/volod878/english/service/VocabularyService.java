package ru.volod878.english.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.volod878.english.ApplicationProperties;
import ru.volod878.english.dto.VocabularyDto;
import ru.volod878.english.model.Vocabulary;
import ru.volod878.english.repository.VocabularyRepository;
import ru.volod878.english.util.VocabularyUtil;

@Service
public class VocabularyService {
    private final VocabularyRepository repository;
    private final ParserService parserService;
    private final ApplicationProperties properties;

    @Autowired
    public VocabularyService(VocabularyRepository repository, ParserService parserService, ApplicationProperties properties) {
        this.repository = repository;
        this.parserService = parserService;
        this.properties = properties;
    }

    public VocabularyDto getByWord(String word) {
        Vocabulary vocabulary = repository.findByWord(word);
        if (vocabulary == null) {
            return parserService.parse(word);
        }
        return VocabularyUtil.asTo(vocabulary);
    }

    public int create(VocabularyDto vocabularyDto) {
        return repository.save(VocabularyUtil.createNewFromTo(properties.getSoundUrl(), vocabularyDto)).getId();
    }
}