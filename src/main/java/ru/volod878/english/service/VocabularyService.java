package ru.volod878.english.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.volod878.english.ApplicationProperties;
import ru.volod878.english.dto.VocabularyDto;
import ru.volod878.english.model.Vocabulary;
import ru.volod878.english.repository.VocabularyRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.volod878.english.util.VocabularyUtil.*;

@RequiredArgsConstructor
@Service
public class VocabularyService implements IVocabularyService {
    private final VocabularyRepository repository;
    private final ParserService parserService;
    private final ApplicationProperties properties;

    @Override
    public VocabularyDto getOrCreateByWord(String word) {
        Vocabulary vocabulary = repository.findByWord(word.toLowerCase());
        return vocabulary == null ? create(parserService.parse(word.toLowerCase())) : asTo(vocabulary);
    }

    @Override
    public Vocabulary getByWord(String word) {
        return repository.findByWord(word.toLowerCase());
    }

    @Override
    public VocabularyDto create(VocabularyDto vocabularyDto) {
        return asTo(repository.save(createNewFromTo(properties.getSoundUrl(), vocabularyDto)));
    }

    // TODO Запускать используя enum
    @Override
    public StreamingResponseBody getMp3(String location, String word) {
        Vocabulary vocabulary = repository.findByWord(word);
        return getStreamingMp3(
                location.equalsIgnoreCase("uk") ?
                        vocabulary.getSoundUkPath() : vocabulary.getSoundUsPath()
        );
    }

    @Override
    public List<VocabularyDto> addAll(List<String> words) {
        return words.stream().map(word -> {
            try {
                return getOrCreateByWord(word);
            } catch (Exception ignored) {
                return new VocabularyDto(word, null, null, null);
            }
        }).filter(dto -> dto.getTranslates() == null).collect(Collectors.toList());
    }

    @Override
    public List<String> getMp3Error(String location, List<String> words) {
        return words.stream()
                .map(word -> {
                    try {
                        if (getMp3(location, word.toLowerCase()) == null) throw new Exception();
                    } catch (Exception ex) {
                        return getByWord(word) == null ? null : word;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}