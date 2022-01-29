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
    private static String urlSoundUk = "https://wooordhunt.ru/word";

    public static void main(String[] args) throws IOException {
        parse("Elevator");
    }

    public static void parse(String word) throws IOException {
        Document document = Jsoup.connect(urlSoundUk + "/" + word).get();
        Elements transcriptionEls = document.select("span.transcription");

        List<String> transcription = transcriptionEls.eachText();
        System.out.println(transcription);

        Element translateEls = document.selectFirst("div.t_inline_en");

        List<String> translates = Arrays.stream(translateEls.text().split(","))
                .map(String::trim)
                .collect(Collectors.toList());
        System.out.println(translates);
    }
}