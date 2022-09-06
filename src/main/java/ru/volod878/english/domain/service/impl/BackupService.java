package ru.volod878.english.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.volod878.english.ApplicationProperties;
import ru.volod878.english.domain.model.Vocabulary;
import ru.volod878.english.domain.repository.VocabularyRepository;
import ru.volod878.english.domain.service.IBackupService;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.volod878.english.util.CsvUtil.*;
import static ru.volod878.english.util.VocabularyUtil.onlyInFirstByWord;

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

    /**
     * Актуализация базы данных.
     * Если backup уже создан, проверяем актуальность данных путем сравнения и обновления записей в БД и backup.
     * Иначе создаем backup БД.
     * Объекты Vocabulary сравниваются по word.
     * Все записи из БД которых нет в backup добавляются в последний.
     * Когда в БД есть запись с word которое отсутствует в backup,
     * но в backup есть запись с id равным id записи из БД, Это запись из backup сохраняется в БД под новым id
     * и файл backup перезаписывается полностью.
     * Все записи из backup которых нет в БД добавляются в последний.
     * Когда в backup есть запись с word которое отсутствует в БД,
     * но в БД есть запись с id равным id записи из backup, Это запись из backup сохраняется в БД под новым id
     * и файл backup перезаписывается полностью.
     */
    private void actualizationDb() throws IOException {
        File backupDb = new File(applicationProperties.getBackupDbPath());
        if (backupDb.exists()) {
            List<Vocabulary> vocabulariesDb = vocabularyRepository.findAll();
            List<Vocabulary> vocabulariesBackup = fromCsv(backupDb, Vocabulary.class, ';');
            List<Vocabulary> onlyInVocabulariesDb = onlyInFirstByWord(vocabulariesDb, vocabulariesBackup);
            if (!onlyInVocabulariesDb.isEmpty()) {
                log.info("Backup vocabulary. {}",
                        onlyInVocabulariesDb.stream().map(Vocabulary::getWord).collect(Collectors.toList()));

                List<Vocabulary> vocabulariesConflict = vocabulariesBackup.stream()
                        .filter(onlyInVocabulariesDb::contains)
                        .collect(Collectors.toList());
                if (vocabulariesConflict.isEmpty()) {
                    updateCsv(backupDb, onlyInVocabulariesDb, ';');
                } else {
                    vocabulariesConflict.forEach(vocabulary -> {
                        log.info("В БД найдена запись с id = {} но word отличается от backup с тем же id. " +
                                        "Backup word = {}, БД word = {}",
                                vocabulary.getId(),
                                vocabulary.getWord(),
                                onlyInVocabulariesDb.stream()
                                        .filter(v -> v.getWord().equals(vocabulary.getWord()))
                                        .findFirst().orElse(null));
                        vocabulary.setId(null);
                    });
                    vocabularyRepository.saveAll(vocabulariesConflict);
                    createCsv(backupDb, vocabularyRepository.findAll(), ';');
                }
            }

            List<Vocabulary> onlyInVocabulariesBackup = onlyInFirstByWord(vocabulariesBackup, vocabulariesDb);
            if (!onlyInVocabulariesBackup.isEmpty()) {
                log.info("Update vocabulary from backup. {}",
                        onlyInVocabulariesBackup.stream().map(Vocabulary::getWord).collect(Collectors.toList()));

                Map<Boolean, List<Vocabulary>> vocabularyConflictAndOnlyBackup = onlyInVocabulariesBackup.stream()
                        .collect(Collectors.partitioningBy(vocabulariesDb::contains));
                final boolean conflict = true;
                vocabularyConflictAndOnlyBackup.get(conflict)
                        .forEach(vocabulary -> {
                            log.info("В backup найдена запись с id = {} но word отличается от БД с тем же id. " +
                                            "Backup word = {}, БД word = {}",
                                    vocabulary.getId(),
                                    vocabulary.getWord(),
                                    vocabulariesDb.stream()
                                            .filter(v -> v.getWord().equals(vocabulary.getWord()))
                                            .findFirst().orElse(null));
                            vocabulary.setId(null);
                        });
                vocabularyRepository.saveAll(onlyInVocabulariesBackup);
                if (!vocabularyConflictAndOnlyBackup.get(conflict).isEmpty()) {
                    createCsv(backupDb, vocabularyRepository.findAll(), ';');
                }
            }
        } else {
            createBackupDataBase();
        }
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
            List<Vocabulary> vocabularies = vocabularyRepository.findAll();
            createCsv(backupDb, vocabularies, ';');
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

    private void createBackupSound(File backupSoundDir) {
        log.info("Выполняется backup звуковых файлов");
        //TODO создать директорию
    }
}
