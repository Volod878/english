package ru.volod878.english.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.volod878.english.ApplicationProperties;
import ru.volod878.english.service.IParserService;
import ru.volod878.english.util.WordParser;
import ru.volod878.english.web.dto.VocabularyDto;

@Slf4j
@RequiredArgsConstructor
@Service
public class ParserService implements IParserService {
    private final ApplicationProperties properties;

    public VocabularyDto parse(String word) {
        log.info("Выполняется поиск слова \"{}\" на внешнем ресурсе", word);
        return WordParser.parse(properties.getWordUrl(), word);
    }
}