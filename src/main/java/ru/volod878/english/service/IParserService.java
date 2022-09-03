package ru.volod878.english.service;

import ru.volod878.english.web.dto.VocabularyDto;

public interface IParserService {
    VocabularyDto parse(String word);
}
