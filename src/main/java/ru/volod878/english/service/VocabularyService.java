package ru.volod878.english.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.volod878.english.ApplicationProperties;
import ru.volod878.english.dto.VocabularyDto;
import ru.volod878.english.model.Vocabulary;
import ru.volod878.english.repository.VocabularyRepository;
import ru.volod878.english.type.Location;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.volod878.english.util.VocabularyUtil.*;

@Slf4j
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

    @Override
    public StreamingResponseBody getMp3(Location location, String word) {
        Vocabulary vocabulary = repository.findByWord(word);
        if (Objects.isNull(vocabulary)) {
            throw new NullPointerException("Слово не найдено");
        }
        return getStreamingMp3(
                location.equals(Location.UK) ?
                        vocabulary.getSoundUkPath() : vocabulary.getSoundUsPath()
        );
    }

    @Override
    public List<VocabularyDto> getAll(List<String> words) {
        return words.stream().map(word -> {
            try {
                return getOrCreateByWord(word);
            } catch (Exception e) {
                log.error("Не удалось найти слово \"{}\"", word, e);
                return new VocabularyDto(word, null, null, null);
            }
        }).collect(Collectors.toList());
    }

    @Override
    public List<String> getMp3Error(Location location, List<String> words) {
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