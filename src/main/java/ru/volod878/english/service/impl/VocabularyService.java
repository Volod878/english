package ru.volod878.english.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.volod878.english.ApplicationProperties;
import ru.volod878.english.domain.enums.Location;
import ru.volod878.english.domain.model.Vocabulary;
import ru.volod878.english.domain.repository.VocabularyRepository;
import ru.volod878.english.domain.service.impl.BackupService;
import ru.volod878.english.service.IParserService;
import ru.volod878.english.service.IVocabularyService;
import ru.volod878.english.util.VocabularyUtil;
import ru.volod878.english.web.dto.VocabularyDto;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static ru.volod878.english.util.VocabularyUtil.*;
import static ru.volod878.english.web.response.enums.WordSourceInfo.IN;
import static ru.volod878.english.web.response.enums.WordSourceInfo.OUT;

@Slf4j
@RequiredArgsConstructor
@Service
public class VocabularyService implements IVocabularyService {
    private final VocabularyRepository repository;
    private final IParserService parserService;
    private final BackupService backupService;
    private final ApplicationProperties properties;

    @Override
    public VocabularyDto getOrCreateByWord(String word) {
        Vocabulary vocabulary = repository.findByWord(word.toLowerCase());
        return Objects.nonNull(vocabulary) ? asTo(vocabulary, IN) : create(parserService.parse(word.toLowerCase()));
    }

    @Override
    public Vocabulary getByWord(String word) {
        return repository.findByWord(word.toLowerCase());
    }

    @Override
    public VocabularyDto create(VocabularyDto vocabularyDto) {
        Vocabulary vocabulary = repository.save(
                createNewFromTo(properties.getSoundUrl(), properties.getSoundDir(), vocabularyDto));
        CompletableFuture.runAsync(backupService::actualization);
        return asTo(vocabulary, OUT);
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
                return VocabularyDto.builder()
                        .word(word)
                        .build();
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

    @Override
    public File getMp3File(Location location, String word) {
        Vocabulary vocabulary = repository.findByWord(word);
        if (Objects.isNull(vocabulary)) {
            throw new NullPointerException("Слово не найдено");
        }
        return VocabularyUtil.getMp3File(
                location.equals(Location.UK) ?
                        vocabulary.getSoundUkPath() : vocabulary.getSoundUsPath()
        );
    }
}