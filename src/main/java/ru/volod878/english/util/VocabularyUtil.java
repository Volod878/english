package ru.volod878.english.util;

import ru.volod878.english.dto.VocabularyDto;
import ru.volod878.english.model.Vocabulary;

public class VocabularyUtil {
    public static Vocabulary createNewFromTo(String soundUrl, VocabularyDto dto) {
        return new Vocabulary(
                null,
                dto.getWord(),
                dto.getTranscriptionUs(),
                dto.getTranscriptionUk(),
                ParserMp3.parseUs(soundUrl, dto.getWord()),
                ParserMp3.parseUk(soundUrl, dto.getWord()),
                dto.getTranslates());
    }

    public static VocabularyDto asTo(Vocabulary vocabulary) {
        return new VocabularyDto(
                vocabulary.getWord(),
                vocabulary.getTranscriptionUs(),
                vocabulary.getTranscriptionUk(),
                vocabulary.getTranslates());
    }
}