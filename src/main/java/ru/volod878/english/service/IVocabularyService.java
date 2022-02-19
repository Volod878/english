package ru.volod878.english.service;

import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.volod878.english.dto.VocabularyDto;
import ru.volod878.english.model.Vocabulary;

import java.util.List;

public interface IVocabularyService {

    VocabularyDto getOrCreateByWord(String word);

    Vocabulary getByWord(String word);

    VocabularyDto create(VocabularyDto vocabularyDto);

    StreamingResponseBody getMp3(String location, String word);

    List<VocabularyDto> addAll(List<String> words);

    List<String> getMp3Error(String location, List<String> words);
}