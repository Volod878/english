package ru.volod878.english.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.volod878.english.service.IBackupService;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class BackupService implements IBackupService {
    @PostConstruct
    private void backup() {

    }
}
