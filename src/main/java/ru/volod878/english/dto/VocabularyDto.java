package ru.volod878.english.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.volod878.english.model.Translate;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VocabularyDto {
    private String word;
    private String transcriptionUs;
    private String transcriptionUk;
    private String soundUs;
    private String soundUk;
    private List<Translate> translates;
    private List<String> translatesList;
}