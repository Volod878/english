package ru.volod878.english.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;

public class ParserMp3 {

    public static String parseUs(String soundUrl, String word) {
        return parse(soundUrl, "us/" + word);
    }

    public static String parseUk(String soundUrl, String word) {
        return parse(soundUrl, "uk/" + word);
    }

    private static String parse(String soundUrl, String word) {
        String wordPath = ".\\" + word.replace("/", "\\") + ".mp3";
        try (BufferedInputStream in = new BufferedInputStream(new URL(soundUrl + "/" + word + ".mp3").openStream());
             FileOutputStream fos = new FileOutputStream(new File(wordPath).getCanonicalFile());
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
        return wordPath;
    }
}
