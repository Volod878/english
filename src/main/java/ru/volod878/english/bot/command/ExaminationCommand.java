package ru.volod878.english.bot.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.volod878.english.service.ISendBotMessage;

import static ru.volod878.english.bot.command.CommandName.EXAMINATION;

@Slf4j
@RequiredArgsConstructor
public class ExaminationCommand implements Command {
    private final ISendBotMessage sendBotMessage;
    public final static String EXAMINATION_MESSAGE = "Команда пока не доступна";

    @Override
    public void execute(Update update) {
        log.info("the {} command is executed", EXAMINATION);
        sendBotMessage.sendMessage(update.getMessage().getChatId().toString(), EXAMINATION_MESSAGE);
    }
}