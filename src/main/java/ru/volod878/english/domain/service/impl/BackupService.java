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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

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
                String[] columnNames = getColumnNames();
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
        try {
            String methodName = "get" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
            Method getFieldValue = Vocabulary.class.getMethod(methodName);
            Object value = getFieldValue.invoke(vocabulary);
            if (value instanceof Integer) {
                return String.valueOf(value);
            } else {
                return (String) value;
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            //TODO другое исключение
            throw new RuntimeException(e);
        }
    }

    private String[] getColumnNames() {
        return Arrays.stream(Vocabulary.class.getDeclaredFields())
                .map(Field::getName)
                .toArray(String[]::new);
    }

    private void createBackupSound(File backupSoundDir) {
        log.info("Выполняется backup звуковых файлов");
        //TODO создать директорию
    }
}
