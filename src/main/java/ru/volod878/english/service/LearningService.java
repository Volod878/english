package ru.volod878.english.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.volod878.english.domain.model.Vocabulary;
import ru.volod878.english.domain.model.WordLearning;
import ru.volod878.english.domain.repository.VocabularyRepository;
import ru.volod878.english.domain.repository.WordLearningRepository;
import ru.volod878.english.web.dto.ExaminationDTO;
import ru.volod878.english.web.response.ExaminationResponse;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@Slf4j
@RequiredArgsConstructor
@Service
public class LearningService implements ILearningService {
    private final VocabularyRepository vocabularyRepository;
    private final WordLearningRepository wordLearningRepository;

    private static final String RESULT = "Вы правильно перевели %d слов(а) из %d";

    @Override
    public List<String> getFewWords(int limit) {
        List<String> words = vocabularyRepository.findWordsWithoutRightAnswer();

//        List<String> rightWords = vocabularyRepository.findWordsWithRightAnswer();
//        System.out.println(rightWords.stream().anyMatch(words::contains));

        Collections.shuffle(words);
        return words.stream().limit(limit).collect(Collectors.toList());
    }

    @Override
    public ExaminationResponse examination(Map<String, String> answers) {
        answers.forEach((key, value) -> {
            if (value.trim().isEmpty()) {
                answers.put(key, null);
            }
        });

        List<Vocabulary> vocabularies = vocabularyRepository.findByWordIn(answers.keySet());

        Map<Boolean, List<ExaminationDTO>> resultMap = vocabularies.stream()
                .map(vocabulary -> new ExaminationDTO(
                        vocabulary.getWord(),
                        answers.get(vocabulary.getWord()),
                        vocabulary.getTranslates()))
                .collect(Collectors.partitioningBy(this::containsTranslation));

        addStatistic(vocabularies, resultMap);

        return new ExaminationResponse(
                String.format(RESULT, resultMap.get(true).size(), vocabularies.size()),
                resultMap.get(true),
                resultMap.get(false));
    }

    private boolean containsTranslation(ExaminationDTO dto) {
        return Objects.nonNull(dto.getAnswer()) &&
                Arrays.stream(dto.getTranslate().split(", "))
                        .anyMatch(translate -> asList(dto.getAnswer().split(", ")).contains(translate));
    }

    private void addStatistic(List<Vocabulary> vocabularies, Map<Boolean, List<ExaminationDTO>> resultMap) {
        List<String> right = resultMap.get(true).stream()
                .map(ExaminationDTO::getWord)
                .collect(Collectors.toList());

        vocabularies.forEach(vocabulary -> wordLearningRepository.save(
                WordLearning.builder()
                        .vocabulary(vocabulary)
                        .answerIsRight(right.contains(vocabulary.getWord()))
                        .insTime(LocalDateTime.now())
                        .build()
        ));
    }
}