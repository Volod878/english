package ru.volod878.english.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.volod878.english.domain.model.Vocabulary;
import ru.volod878.english.exception.FailedAudioStreamingException;
import ru.volod878.english.web.dto.VocabularyDto;
import ru.volod878.english.web.response.enums.WordSourceInfo;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class VocabularyUtil {

    public static Vocabulary createNewFromTo(String soundUrl, String soundDir, VocabularyDto dto) {
        return new Vocabulary(
                null,
                dto.getWord(),
                dto.getTranscriptionUs(),
                dto.getTranscriptionUk(),
                Mp3Parser.parseUs(soundUrl, soundDir, dto.getWord()),
                Mp3Parser.parseUk(soundUrl, soundDir, dto.getWord()),
                dto.getTranslates());
    }

    public static VocabularyDto asTo(Vocabulary vocabulary, WordSourceInfo wordSourceInfo) {
        return new VocabularyDto(
                vocabulary.getWord(),
                vocabulary.getTranscriptionUs(),
                vocabulary.getTranscriptionUk(),
                vocabulary.getTranslates(),
                wordSourceInfo);
    }

    public static StreamingResponseBody getStreamingMp3(String wordPath) {
        try {
            InputStream audioFileStream = Files.newInputStream(Paths.get(wordPath));
            return (os) -> readAndWriteStream(audioFileStream, os);
        } catch (IOException e) {
            log.error("Неудачная попытка получить аудио файл. wordPath = {}", wordPath, e);
            throw new FailedAudioStreamingException("Неудачная попытка получить аудио файл");
        }
    }

    private static void readAndWriteStream(final InputStream is, OutputStream os) throws IOException {
        byte[] data = new byte[2048];
        int read;
        while ((read = is.read(data)) > 0) {
            os.write(data, 0, read);
        }
        os.flush();
    }

    public static File getMp3File(String wordPath) {
        return new File(wordPath);
    }

    public static List<Vocabulary> onlyInFirstByWord(List<Vocabulary> vocabulariesFirst,
                                                     List<Vocabulary> vocabulariesSecond) {
        return vocabulariesFirst.stream()
                .filter(vocabularyFirst -> vocabulariesSecond.stream()
                        .noneMatch(vocabularySecond -> vocabularySecond.getWord().equals(vocabularyFirst.getWord())))
                .collect(Collectors.toList());
    }
}