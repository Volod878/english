package ru.volod878.english.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.volod878.english.exception.FailedAudioStreamingException;
import ru.volod878.english.service.IVocabularyService;
import ru.volod878.english.type.Location;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/audio")
public class AudioController {
    private final IVocabularyService vocabularyService;

    @GetMapping("/{location}/mp3")
    public StreamingResponseBody getMp3(@PathVariable("location") Location location, @RequestParam String fileName) {
        return vocabularyService.getMp3(location, fileName);
    }

    @GetMapping("/{location}/mp3error")
    public List<String> getError(@PathVariable Location location, @RequestBody List<String> words) {
        return vocabularyService.getMp3Error(location, words);
    }

    @ExceptionHandler(FailedAudioStreamingException.class)
    public ResponseEntity<String> handleFailedAudioStreamingException(FailedAudioStreamingException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}