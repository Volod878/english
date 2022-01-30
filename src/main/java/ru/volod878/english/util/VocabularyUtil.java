package ru.volod878.english.util;

import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.volod878.english.dto.VocabularyDto;
import ru.volod878.english.model.Vocabulary;

import java.io.*;

public class VocabularyUtil {
    public static Vocabulary createNewFromTo(String soundUrl, VocabularyDto dto) {
        return new Vocabulary(
                null,
                dto.getWord(),
                dto.getTranscriptionUs(),
                dto.getTranscriptionUk(),
                Mp3Parser.parseUs(soundUrl, dto.getWord()),
                Mp3Parser.parseUk(soundUrl, dto.getWord()),
                dto.getTranslates());
    }

    public static VocabularyDto asTo(Vocabulary vocabulary) {
        return new VocabularyDto(
                vocabulary.getWord(),
                vocabulary.getTranscriptionUs(),
                vocabulary.getTranscriptionUk(),
                vocabulary.getTranslates());
    }

    public static StreamingResponseBody getStreamingMp3(String wordPath) {
        try {
            InputStream audioFileStream = new FileInputStream(wordPath);
            return (os) -> readAndWrite(audioFileStream, os);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void readAndWrite(final InputStream is, OutputStream os) throws IOException {
        byte[] data = new byte[2048];
        int read;
        while ((read = is.read(data)) > 0) {
            os.write(data, 0, read);
        }
        os.flush();
    }
}