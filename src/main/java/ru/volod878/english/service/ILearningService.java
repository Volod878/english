package ru.volod878.english.service;

import ru.volod878.english.domain.model.User;
import ru.volod878.english.domain.model.WordLearning;
import ru.volod878.english.web.response.ExaminationResponse;

import java.util.List;
import java.util.Map;

public interface ILearningService {

    List<String> getFewWords(int limit);

    ExaminationResponse examination(Map<String, String> answers);

    ExaminationResponse examination(Map<String, String> answers, User user);

    List<WordLearning> info();
}