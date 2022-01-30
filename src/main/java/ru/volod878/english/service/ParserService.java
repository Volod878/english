package ru.volod878.english.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.volod878.english.ApplicationProperties;
import ru.volod878.english.dto.VocabularyDto;
import ru.volod878.english.util.WordParser;

@Service
public class ParserService {
    private final ApplicationProperties properties;

    @Autowired
    public ParserService(ApplicationProperties properties) {
        this.properties = properties;
    }

    public VocabularyDto parse(String word) {
        return WordParser.parse(properties.getWordUrl(), word);
    }
}