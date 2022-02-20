package ru.volod878.english.service;

import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.volod878.english.web.dto.VocabularyDto;
import ru.volod878.english.domain.model.Vocabulary;
import ru.volod878.english.domain.enums.Location;

import java.util.List;

public interface IVocabularyService {

    VocabularyDto getOrCreateByWord(String word);

    Vocabulary getByWord(String word);

    VocabularyDto create(VocabularyDto vocabularyDto);

    StreamingResponseBody getMp3(Location location, String word);

    List<VocabularyDto> getAll(List<String> words);

    List<String> getMp3Error(Location location, List<String> words);
}