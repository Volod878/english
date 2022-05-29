package ru.volod878.english.bot.command;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.volod878.english.service.ISendBotMessage;

@RequiredArgsConstructor
public class UnknownCommand implements Command {
    private final ISendBotMessage sendBotMessage;
    public static final String UNKNOWN_MESSAGE = "Упс...\n" +
            "Кажется перевести это";

    @Override
    public void execute(Update update) {
        sendBotMessage.sendMessage(update.getMessage().getChatId().toString(), UNKNOWN_MESSAGE);
    }
}