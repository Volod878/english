package ru.volod878.english.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

public class ParserMp3 {
    @Value("${url-sound}")
    private static String urlSound = "https://wooordhunt.ru/data/sound/sow";

    public static void parseUs(String word) {
        parse(urlSound, "us/" + word);
    }

    public static void parseUk(String word) {
        parse(urlSound, "uk/" + word);
    }

    private static void parse(String urlSound, String word) {
        try (BufferedInputStream in = new BufferedInputStream(new URL(urlSound + "/" + word + ".mp3").openStream());
             FileOutputStream fos = new FileOutputStream(new File(".\\" + word.replace("/", "\\") + ".mp3").getCanonicalFile());
             BufferedOutputStream bout = new BufferedOutputStream(fos, 1024)) {
            byte[] data = new byte[1024];
            int x;
            while ((x = in.read(data, 0, 1024)) >= 0) {
                bout.write(data, 0, x);
            }
            fos.flush();
            bout.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
