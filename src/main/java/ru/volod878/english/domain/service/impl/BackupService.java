package ru.volod878.english.domain.service.impl;

import liquibase.util.csv.CSVWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.volod878.english.ApplicationProperties;
import ru.volod878.english.domain.model.Vocabulary;
import ru.volod878.english.domain.repository.VocabularyRepository;
import ru.volod878.english.domain.service.IBackupService;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис предоставляет возможность сохранение актуального состояние и восстановления данных.
 * В текущей реализации выполняется backup таблицы vocabulary
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BackupService implements IBackupService {
    private final ApplicationProperties applicationProperties;
    private final VocabularyRepository vocabularyRepository;

    /**
     * Перед запуском приложения необходимо соотнести текущие состояние БД и backup.
     * В случае не соответствия производится актуализация БД или backup.
     */
    @PostConstruct
    private void backup() throws IOException {
        File backupDirectory = new File(applicationProperties.getBackupPath());
        if (backupDirectory.exists()) {
            actualizationDb();
            actualizationSound();
        } else {
            createBackupDate(backupDirectory);
        }
    }

    private void actualizationDb() throws IOException {
        File backupDb = new File(applicationProperties.getBackupDbPath());
        if (backupDb.exists()) {

        } else {
            createBackupDataBase();
        }
    }

    private void actualizationSound() {
        applicationProperties.getBackupSoundPaths().forEach(soundPath -> {
            File backupSoundDir = new File(soundPath);
            if (backupSoundDir.exists()) {

            } else {
                createBackupSound(backupSoundDir);
            }
        });
    }

    private void createBackupDate(File backupDirectory) throws IOException {
        log.info("Выполняется backup всех данных");
        if (backupDirectory.createNewFile()) {
            createBackupDataBase();
            applicationProperties.getBackupSoundPaths()
                    .forEach(soundPath -> createBackupSound(new File(soundPath)));
        } else {
            log.error("Не удалось создать директорию {}", backupDirectory.getPath());
        }
    }

    private void createBackupDataBase() throws IOException {
        log.info("Выполняется backup Базы данных");
        File backupDb = new File(applicationProperties.getBackupDbPath());

        if ((backupDb.getParentFile().exists() || backupDb.getParentFile().createNewFile())
                &&
                backupDb.createNewFile()) {
            try (CSVWriter writer = new CSVWriter(new FileWriter(backupDb), ';')) {
                String[] columnNames = getColumnName();
                writer.writeNext(columnNames);
                vocabularyRepository.findAll().forEach(vocabulary -> {
                    String[] vocabularyRow = new String[columnNames.length];
                    for (int i = 0; i < columnNames.length; i++) {
                        vocabularyRow[i] = getValueField(columnNames[i], vocabulary);
                    }
                    writer.writeNext(vocabularyRow);
                });
            } catch (Exception ignored) {
            }
        }
    }

    private String getValueField(String columnName, Vocabulary vocabulary) {
        String value;
        switch (columnName) {
            case ("id"):
                value = String.valueOf(vocabulary.getId());
                break;
            case ("word"):
                value = vocabulary.getWord();
                break;
            case ("transcriptionUs"):
                value = vocabulary.getTranscriptionUs();
                break;
            case ("transcriptionUk"):
                value = vocabulary.getTranscriptionUk();
                break;
            case ("soundUsPath"):
                value = vocabulary.getSoundUsPath();
                break;
            case ("soundUkPath"):
                value = vocabulary.getSoundUkPath();
                break;
            case ("translates"):
                value = vocabulary.getTranslates();
                break;
            default:
                //TODO другое исключение
                throw new RuntimeException();
        }
        return value;
    }

    private String[] getColumnName() {
        List<String> fieldNames = Arrays.stream(Vocabulary.class.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toList());
        String[] fieldArray = new String[fieldNames.size()];
        return fieldNames.toArray(fieldArray);
    }

    private void createBackupSound(File backupSoundDir) {
        log.info("Выполняется backup звуковых файлов");
        //TODO создать директорию
    }
}
