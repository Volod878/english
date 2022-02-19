package ru.volod878.english.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.volod878.english.ApplicationProperties;
import ru.volod878.english.dto.VocabularyDto;
import ru.volod878.english.util.WordParser;

@Slf4j
@RequiredArgsConstructor
@Service
public class ParserService {
    private final ApplicationProperties properties;

    public VocabularyDto parse(String word) {
        log.info("Выполняется поиск слова \"{}\" на внешнем ресурсе", word);
        return WordParser.parse(properties.getWordUrl(), word);
    }
}