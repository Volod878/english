package ru.volod878.english.util;

import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.net.URL;

public class ParserMp3 {
    @Value("${url-sound-us}")
    private static String urlSoundUs;
    @Value("${url-sound-uk}")
    private static String urlSoundUk;

    public static void parseUs(String word) {
        parse(urlSoundUs, word);
    }

    public static void parseUk(String word) {
        parse(urlSoundUk, word);
    }

    private static void parse(String urlSound, String word) {
        try (BufferedInputStream in = new BufferedInputStream(new URL(urlSound + "/" + word + ".mp3").openStream());
             FileOutputStream fos = new FileOutputStream(word + ".mp3");
             BufferedOutputStream bout = new BufferedOutputStream(fos, 1024)) {

            byte[] data = new byte[1024];
            int x;
            while ((x = in.read(data, 0, 1024)) >= 0) {
                bout.write(data, 0, x);
            }
        } catch (Exception ignored) {
        }
    }
}
