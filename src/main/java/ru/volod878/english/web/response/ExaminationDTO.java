package ru.volod878.english.web.response;

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
}