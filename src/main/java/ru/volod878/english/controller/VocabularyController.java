package ru.volod878.english.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.volod878.english.dto.VocabularyDto;
import ru.volod878.english.service.IVocabularyService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vocabulary")
public class VocabularyController {
    private final IVocabularyService vocabularyService;

    @GetMapping
    public VocabularyDto get(@RequestParam String word) {
        return vocabularyService.getOrCreateByWord(word);
    }

    @PostMapping()
    public List<VocabularyDto> getAll(@RequestBody List<String> words) {
        return vocabularyService.getAll(words);
    }
}
