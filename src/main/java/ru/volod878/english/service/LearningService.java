package ru.volod878.english.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.volod878.english.repository.VocabularyRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class LearningService implements ILearningService {
    private final VocabularyRepository vocabularyRepository;

    @Override
    public List<String> getFewWords(int limit) {
        List<String> words = vocabularyRepository.findAllWords();
        Collections.shuffle(words);
        return words.stream().limit(limit).collect(Collectors.toList());
    }
}