package ru.volod878.english.service.impl.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.volod878.english.bot.EnglishBot;
import ru.volod878.english.bot.command.ExaminationCommand;
import ru.volod878.english.domain.repository.UserRepository;

import static ru.volod878.english.bot.command.CommandName.EXAMINATION;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExamJobService {
    private final UserRepository userRepository;
    private final EnglishBot englishBot;

    @Scheduled(cron = "${job.scheduled-exam.cron}")
    public void scheduledExam() {
        log.info("start scheduledExam");
        userRepository.findAll().forEach(user -> {
            ExaminationCommand command = (ExaminationCommand) englishBot.getCommandContainer()
                    .retrieveCommand(EXAMINATION.getCommandName());
            command.startExam(String.valueOf(user.getTelegramUserId()));
        });
    }
}
