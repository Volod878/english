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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.volod878.english.util.CsvUtil.*;
import static ru.volod878.english.util.VocabularyUtil.onlyInFirstByWord;

/**
 * Сервис предоставляет возможность сохранение актуального состояние и восстановления данных.
 * Во время развертывания приложения на новом сервере
 * В текущей реализации выполняется backup таблицы vocabulary.
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
            actualization();
        } else {
            createBackupDate(backupDirectory);
        }
    }

    /**
     * Актуализация базы данных.
     * Если backup уже создан, проверяем актуальность данных путем сравнения и обновления записей в БД и backup.
     * Иначе создаем backup БД.
     * Объекты Vocabulary сравниваются по word.
     * Возможные кейсы, когда нужно внести изменения в backup:
     * 1. В backup отсутствует запись, которая есть в БД;
     * 2. Если в БД есть запись таким же id как в backup, но поле word отличается;
     * 3. Если в БД есть запись таким же полем word как в backup, но id отличается.
     * Возможные кейсы, когда нужно внести изменения в БД:
     * 1. В БД отсутствует запись, которая есть в backup;
     * 2. Если в БД есть запись таким же id как в backup, но поле word отличается;
     * 3. Если в БД есть запись таким же полем word как в backup, но id отличается.
     * Все записи из БД которых нет в backup добавляются в последний.
     * Когда в БД есть запись с word которое отсутствует в backup,
     * но в backup есть запись с id равным id записи из БД, Это запись из backup сохраняется в БД под новым id
     * и файл backup перезаписывается полностью.
     * Все записи из backup которых нет в БД добавляются в последний.
     * Когда в backup есть запись с word которое отсутствует в БД,
     * но в БД есть запись с id равным id записи из backup, Это запись из backup сохраняется в БД под новым id
     * и файл backup перезаписывается полностью.
     */
    @Override
    public void actualization() {
        try {
            File backupDb = new File(applicationProperties.getBackupDbPath());
            if (backupDb.exists()) {
                List<Vocabulary> vocabulariesDb = vocabularyRepository.findAll();
                List<Vocabulary> vocabulariesBackup = fromCsv(backupDb, Vocabulary.class, ';');
                List<Vocabulary> vocabulariesOnlyInDb = onlyInFirstByWord(vocabulariesDb, vocabulariesBackup);
                if (!vocabulariesOnlyInDb.isEmpty()) {
                    log.info("Backup vocabulary. {}",
                            vocabulariesOnlyInDb.stream().map(Vocabulary::getWord).collect(Collectors.toList()));

                    List<Vocabulary> vocabulariesConflict = vocabulariesBackup.stream()
                            .filter(vocabulariesOnlyInDb::contains)
                            .collect(Collectors.toList());
                    if (vocabulariesConflict.isEmpty()) {
                        updateCsv(backupDb, vocabulariesOnlyInDb, ';');
                    } else {
                        vocabulariesConflict.forEach(vocabulary ->
                                log.info("В БД найдена запись с id = {} у которой word отличается от записи backup с тем же id. " +
                                                "Backup word = {}, БД word = {}",
                                        vocabulary.getId(),
                                        vocabulary.getWord(),
                                        vocabulariesOnlyInDb.stream()
                                                .filter(v -> v.getId().equals(vocabulary.getId()))
                                                .findFirst()
                                                .map(Vocabulary::getWord)
                                                .orElse(null)));
                        List<Vocabulary> vocabularies = vocabularyRepository.findAll();
                        vocabularies.sort(Comparator.comparingInt(Vocabulary::getId));
                        createCsv(backupDb, vocabularies, ';');
                    }
                }

                List<Vocabulary> vocabulariesOnlyInBackup = onlyInFirstByWord(vocabulariesBackup, vocabulariesDb);
                if (!vocabulariesOnlyInBackup.isEmpty()) {
                    log.info("Update vocabulary from backup. {}",
                            vocabulariesOnlyInBackup.stream().map(Vocabulary::getWord).collect(Collectors.toList()));

                    Map<Boolean, List<Vocabulary>> vocabularyConflictAndOnlyBackup = vocabulariesOnlyInBackup.stream()
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
                    vocabularyRepository.saveAll(vocabulariesOnlyInBackup);
                    if (!vocabularyConflictAndOnlyBackup.get(conflict).isEmpty()) {
                        List<Vocabulary> vocabularies = vocabularyRepository.findAll();
                        vocabularies.sort(Comparator.comparingInt(Vocabulary::getId));
                        createCsv(backupDb, vocabularies, ';');
                    }
                }
            } else {
                createBackupDataBase();
            }
        } catch (IOException ex) {
            log.error("Ошибка чтения или записи файла", ex);
        }
    }

    private void createBackupDate(File backupDirectory) throws IOException {
        log.info("Выполняется backup всех данных");
        if (backupDirectory.createNewFile()) {
            createBackupDataBase();
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
            vocabularies.sort(Comparator.comparingInt(Vocabulary::getId));
            createCsv(backupDb, vocabularies, ';');
        }
    }
}
