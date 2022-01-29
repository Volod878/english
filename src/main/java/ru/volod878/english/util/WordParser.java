package ru.volod878.english.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.volod878.english.dto.VocabularyDto;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class WordParser {
    @Value("${url-word-transcription-and-translate}")
    private static String url = "https://wooordhunt.ru/word";

    private static final Map<String, Document> documents = new ConcurrentHashMap<>();

    public static VocabularyDto parse(String word) {
        documents.computeIfAbsent(word, key -> {
            try {
                ParserMp3.parseUs(word);
                ParserMp3.parseUk(word);
                return Jsoup.connect(url + "/" + word).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
        VocabularyDto dto = new VocabularyDto();
        dto.setWord(word);
        dto.setTranscriptionUs(parseTranscriptions(word).get(0));
        dto.setTranscriptionUk(parseTranscriptions(word).get(1));
        dto.setTranslatesList(parseTranslates(word));
        return dto;
    }

    private static List<String> parseTranscriptions(String word) {
        return getTranscriptions(documents.get(word).select("span.transcription"));
    }

    private static List<String> parseTranslates(String word) {
        return getTranslates(Objects.requireNonNull(documents.get(word).selectFirst("div.t_inline_en")));
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