package ru.volod878.english.contraller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.volod878.english.dto.VocabularyDto;
import ru.volod878.english.service.VocabularyService;

@RestController
public class VocabularyController {
    VocabularyService vocabularyService;

    @Autowired
    public VocabularyController(VocabularyService vocabularyService) {
        this.vocabularyService = vocabularyService;
    }

    @GetMapping
    public VocabularyDto get(@RequestParam String word) {
        return vocabularyService.getByWord(word);
    }
}
