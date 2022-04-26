package ru.volod878.english.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.volod878.english.web.response.enums.WordSourceInfo;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VocabularyDto {
    private String word;
    private String transcriptionUs;
    private String transcriptionUk;
    private String translates;
    private WordSourceInfo wordSourceInfo;
}