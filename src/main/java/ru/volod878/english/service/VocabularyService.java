package ru.volod878.english.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.volod878.english.dto.VocabularyDto;
import ru.volod878.english.model.Vocabulary;
import ru.volod878.english.repository.VocabularyRepository;
import ru.volod878.english.util.VocabularyUtil;

@Service
public class VocabularyService {
    private final VocabularyRepository repository;
    private final ParserService parserService;

    @Autowired
    public VocabularyService(VocabularyRepository repository, ParserService parserService) {
        this.repository = repository;
        this.parserService = parserService;
    }

    public VocabularyDto getByWord(String word) {
        Vocabulary vocabulary = repository.findByWord(word);
        if (vocabulary == null) {
            return parserService.parse(word);
        }
        return VocabularyUtil.asTo(vocabulary);
    }

    public int create(VocabularyDto vocabularyDto) {
        return repository.save(VocabularyUtil.createNewFromTo(vocabularyDto)).getId();
    }
}