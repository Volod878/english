package ru.volod878.english.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.volod878.english.domain.model.Vocabulary;
import ru.volod878.english.domain.repository.VocabularyRepository;
import ru.volod878.english.web.dto.ExaminationDTO;
import ru.volod878.english.web.response.ExaminationResponse;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class LearningService implements ILearningService {
    private final VocabularyRepository vocabularyRepository;

    private static final String RESULT = "Вы перевели правильно %d слов(а) из %d";

    @Override
    public List<String> getFewWords(int limit) {
        List<String> words = vocabularyRepository.findAllWords();
        Collections.shuffle(words);
        return words.stream().limit(limit).collect(Collectors.toList());
    }

    @Override
    public ExaminationResponse examination(Map<String, String> answers) {
        answers.forEach((key, value) -> {
            if (value.trim().isEmpty()) {
                answers.put(key, null);
            }});

        List<Vocabulary> vocabularies = vocabularyRepository.findByWordIn(answers.keySet());

        Map<Boolean, List<ExaminationDTO>> resultMap = vocabularies.stream()
                .map(vocabulary -> new ExaminationDTO(
                        vocabulary.getWord(),
                        answers.get(vocabulary.getWord()),
                        vocabulary.getTranslates()))
                .collect(Collectors.partitioningBy(dto ->
                        Objects.nonNull(dto.getAnswer()) && dto.getTranslate().contains(dto.getAnswer())));

        return new ExaminationResponse(
                String.format(RESULT, resultMap.get(true).size(), vocabularies.size()),
                resultMap.get(true),
                resultMap.get(false));
    }
}