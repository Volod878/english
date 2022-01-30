package ru.volod878.english.contraller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.volod878.english.dto.VocabularyDto;
import ru.volod878.english.service.VocabularyService;

import java.util.List;

@RestController
public class VocabularyController {
    private final VocabularyService vocabularyService;

    @Autowired
    public VocabularyController(VocabularyService vocabularyService) {
        this.vocabularyService = vocabularyService;
    }

    @GetMapping
    public VocabularyDto get(@RequestParam String word) {
        return vocabularyService.getByWord(word);
    }

    @GetMapping("/{location}/mp3")
    public StreamingResponseBody getMp3(@PathVariable String location, @RequestParam String fileName) {
        return vocabularyService.getMp3(location, fileName);

    }

    @PostMapping()
    public List<VocabularyDto> addAll(@RequestBody List<String> words) {
        return vocabularyService.addAll(words);
    }
}
