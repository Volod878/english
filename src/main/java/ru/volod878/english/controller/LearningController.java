package ru.volod878.english.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.volod878.english.service.ILearningService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/learning")
public class LearningController {
    private final ILearningService service;

    @GetMapping("/words/{limit}")
    public List<String> getFewWords(@PathVariable int limit) {
        return service.getFewWords(limit);
    }
}