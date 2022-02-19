package ru.volod878.english.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Mp3Parser {
    private static final Logger log = LoggerFactory.getLogger(Mp3Parser.class);

    public static String parseUs(String soundUrl, String word) {
        return parse(soundUrl, "/us/" + word);
    }

    public static String parseUk(String soundUrl, String word) {
        return parse(soundUrl, "/uk/" + word);
    }

    private static String parse(String soundUrl, String word) {
        String wordPath = "." + word + ".mp3";

        try (ReadableByteChannel readableByteChannel = Channels.newChannel(new URL(soundUrl + word + ".mp3").openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(wordPath)) {
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            log.error("Неудачный парсинг аудио файла. word = {}, wordPath ={}", word, wordPath, e);
        }
        return wordPath;
    }
}
