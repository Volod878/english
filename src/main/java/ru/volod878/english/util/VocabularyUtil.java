package ru.volod878.english.util;

import ru.volod878.english.dto.VocabularyDto;
import ru.volod878.english.model.Vocabulary;

public class VocabularyUtil {
    public static Vocabulary createNewFromTo(VocabularyDto dto) {
        return new Vocabulary(
                null,
                dto.getWord(),
                dto.getTranscriptionUs(),
                dto.getTranscriptionUk(),
                dto.getSoundUs(),
                dto.getSoundUk(),
                dto.getTranslates());
    }

    public static VocabularyDto asTo(Vocabulary vocabulary) {
        return new VocabularyDto(
                vocabulary.getWord(),
                vocabulary.getTranscriptionUs(),
                vocabulary.getTranscriptionUk(),
                vocabulary.getSoundUsPath(),
                vocabulary.getSoundUkPath(),
                vocabulary.getTranslates());
    }
}