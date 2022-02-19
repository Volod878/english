package ru.volod878.english;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class ApplicationProperties {
    @Value("${url-word-transcription-and-translate}")
    private String wordUrl;
    @Value("${url-sound}")
    private String soundUrl;
}