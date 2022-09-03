package ru.volod878.english;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class ApplicationProperties {
    @Value("${url-word-transcription-and-translate}")
    private String wordUrl;
    @Value("${url-sound}")
    private String soundUrl;
    @Value("${backup-path}")
    private String backupPath;
    @Value("${backup-db-path}")
    private String backupDbPath;
    @Value("${backup-sound-paths}")
    private List<String> backupSoundPaths;
}