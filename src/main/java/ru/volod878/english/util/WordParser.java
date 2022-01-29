package ru.volod878.english.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WordParser {
    @Value("${url-word-transcription-and-translate}")
    private static String urlSoundUk;

    public static List<String> parseTranscriptions(String word) throws IOException {
        Document document = Jsoup.connect(urlSoundUk + "/" + word).get();
        return getTranscriptions(document.select("span.transcription"));
    }

    public static List<String> parseTranslates(String word) throws IOException {
        Document document = Jsoup.connect(urlSoundUk + "/" + word).get();
        return getTranslates(document.selectFirst("div.t_inline_en"));
    }

    private static List<String> getTranscriptions(Elements elements) {
        return elements.eachText();
    }

    private static List<String> getTranslates(Element element) {
        return Arrays.stream(element.text().split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }
}