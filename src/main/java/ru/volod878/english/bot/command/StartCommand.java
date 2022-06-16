package ru.volod878.english.bot.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.volod878.english.service.ISendBotMessage;

import static ru.volod878.english.bot.command.CommandName.START;

@Slf4j
@RequiredArgsConstructor
public class StartCommand implements Command {
    private final ISendBotMessage sendBotMessage;
    public final static String START_MESSAGE = "Привет!\n" +
            "Я помогу тебе выучить английские слова.\n" +
            "Напиши мне любое английское слово и я постараюсь его перевести.";

    @Override
    public void execute(Update update) {
        log.info("the {} command is executed", START);
        sendBotMessage.sendMessage(update.getMessage().getChatId().toString(), START_MESSAGE);
    }
}