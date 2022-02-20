package ru.volod878.english.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.volod878.english.model.Vocabulary;
import ru.volod878.english.repository.VocabularyRepository;
import ru.volod878.english.web.dto.ExaminationDTO;
import ru.volod878.english.web.response.ExaminationResponse;

import java.util.Collections;
import java.util.List;
import java.util.Map;
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

    @Override
    public ExaminationResponse examination(Map<String, String> answers) {
        List<Vocabulary> vocabularies = vocabularyRepository.findByWordIn(answers.keySet());

        Map<Boolean, List<ExaminationDTO>> resultMap = vocabularies.stream()
                .map(vocabulary -> new ExaminationDTO(
                        vocabulary.getWord(),
                        answers.get(vocabulary.getWord()),
                        vocabulary.getTranslates()))
                .collect(Collectors.partitioningBy(dto -> dto.getTranslate().contains(dto.getAnswer())));

        String result = "Вы перевели правильно %d слов(а) из %d";
        return new ExaminationResponse(
                String.format(result, resultMap.get(true).size(), vocabularies.size()),
                resultMap.get(true),
                resultMap.get(false));
    }
}