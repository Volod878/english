package ru.volod878.english.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import ru.volod878.english.dto.VocabularyDto;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WordParser {

    private static final Map<String, Document> documents = new ConcurrentHashMap<>();

    public static VocabularyDto parse(String url, String word) {
        documents.computeIfAbsent(word, key -> {
            try {
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
        dto.setTranslates(parseTranslates(word));
        return dto;
    }

    private static List<String> parseTranscriptions(String word) {
        return getTranscriptions(documents.get(word).select("span.transcription"));
    }

    private static String parseTranslates(String word) {
        return getTranslates(Objects.requireNonNull(documents.get(word).selectFirst("div.t_inline_en")));
    }

    private static List<String> getTranscriptions(Elements elements) {
        return elements.eachText();
    }

    private static String getTranslates(Element element) {
        return element.text();
    }
}