package ru.volod878.english.bot.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.volod878.english.service.ISendBotMessage;

import static ru.volod878.english.bot.command.CommandName.WORD_INFO;

@Slf4j
@RequiredArgsConstructor
public class WordInfoCommand implements Command {
    private final ISendBotMessage sendBotMessage;
    public final static String WORD_INFO_MESSAGE = "Команда пока не доступна";

    @Override
    public void execute(Update update) {
        log.info("the {} command is executed", WORD_INFO);
        sendBotMessage.sendMessage(update.getMessage().getChatId().toString(), WORD_INFO_MESSAGE);
    }
}