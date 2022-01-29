package ru.volod878.english.service;

import org.springframework.stereotype.Service;
import ru.volod878.english.dto.VocabularyDto;
import ru.volod878.english.repository.VocabularyRepository;
import ru.volod878.english.util.VocabularyUtil;

@Service
public class VocabularyService {
    private VocabularyRepository repository;

    public VocabularyDto getByWord(String word) {
        return VocabularyUtil.asTo(repository.findByWord(word));
    }

    public int create(VocabularyDto vocabularyDto) {
        return repository.save(VocabularyUtil.createNewFromTo(vocabularyDto)).getId();
    }
}