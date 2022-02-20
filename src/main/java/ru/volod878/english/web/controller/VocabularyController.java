package ru.volod878.english.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.volod878.english.dto.VocabularyDto;
import ru.volod878.english.exception.FailedToCreateDtoException;
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

    @ExceptionHandler(FailedToCreateDtoException.class)
    public ResponseEntity<String> handleFailedToCreateDtoException(FailedToCreateDtoException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
