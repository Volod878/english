package ru.volod878.english.util;

import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

@Slf4j
public class Mp3Parser {

    public static String parseUs(String soundUrl, String soundDir, String word) {
        return parse(soundUrl, soundDir, "/us/" + word);
    }

    public static String parseUk(String soundUrl, String soundDir, String word) {
        return parse(soundUrl, soundDir, "/uk/" + word);
    }

    private static String parse(String soundUrl, String soundDir, String word) {
        String wordPath = soundDir + word + ".mp3";

        try (ReadableByteChannel readableByteChannel = Channels.newChannel(new URL(soundUrl + word + ".mp3").openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(wordPath)) {
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            log.error("Неудачный парсинг аудио файла. wordPath = {}", wordPath, e);
        }
        return wordPath;
    }
}
