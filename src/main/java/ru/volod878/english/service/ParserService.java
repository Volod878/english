package ru.volod878.english.service;

import org.springframework.stereotype.Service;
import ru.volod878.english.dto.VocabularyDto;
import ru.volod878.english.util.WordParser;

@Service
public class ParserService {
    public VocabularyDto parse(String word) {
        return WordParser.parse(word);
    }
}