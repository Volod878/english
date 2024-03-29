package ru.volod878.english.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExaminationDTO {
    private String word;
    private String answer;
    private String translate;

    @Override
    public String toString() {
        return word + '\n' +
                "ваш ответ: " + answer + '\n' +
                "правильный перевод: " + translate + '\n';
    }
}