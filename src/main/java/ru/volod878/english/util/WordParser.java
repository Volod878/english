package ru.volod878.english.util;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import ru.volod878.english.dto.VocabularyDto;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class WordParser {
    private static Document document;

    public static VocabularyDto parse(String url, String word) {
        VocabularyDto dto = new VocabularyDto();
        try {
            document = Jsoup.connect(url + "/" + word).get();
            dto.setWord(word);
            dto.setTranscriptionUs(parseTranscriptions().get(0));
            dto.setTranscriptionUk(parseTranscriptions().get(1));
            dto.setTranslates(parseTranslates());
        } catch (Exception e) {
            log.error("Не удалось создать dto для слова \"{}\". url = {}", word, url, e);
            return null;
        }
        return dto;
    }

    private static List<String> parseTranscriptions() {
        return getTranscriptions(document.select("span.transcription"));
    }

    private static String parseTranslates() {
        return getTranslates(Objects.requireNonNull(document.selectFirst("div.t_inline_en")));
    }

    private static List<String> getTranscriptions(Elements elements) {
        return elements.eachText();
    }

    private static String getTranslates(Element element) {
        return element.text();
    }
}