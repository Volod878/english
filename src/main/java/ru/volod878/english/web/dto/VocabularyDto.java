package ru.volod878.english.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VocabularyDto {
    private String word;
    private String transcriptionUs;
    private String transcriptionUk;
    private String translates;
}