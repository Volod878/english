package ru.volod878.english.service;

import ru.volod878.english.web.response.ExaminationResponse;

import java.util.List;
import java.util.Map;

public interface ILearningService {

    List<String> getFewWords(int limit);

    ExaminationResponse examination(Map<String, String> answers);
}