package ru.volod878.english.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.volod878.english.service.ILearningService;
import ru.volod878.english.web.response.ExaminationResponse;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/learning")
public class LearningController {
    private final ILearningService service;

    @GetMapping("/words/{limit}")
    public List<String> getFewWords(@PathVariable int limit) {
        return service.getFewWords(limit);
    }

    @PostMapping("/examination")
    public ResponseEntity<ExaminationResponse> examination(@RequestBody Map<String, String> answers) {
        return new ResponseEntity<>(service.examination(answers), HttpStatus.OK);
    }
}