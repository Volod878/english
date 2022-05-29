package ru.volod878.english.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.volod878.english.bot.command.*;
import ru.volod878.english.service.ISendBotMessage;
import ru.volod878.english.service.IVocabularyService;
import ru.volod878.english.service.SendBotMessage;

import java.util.concurrent.ConcurrentHashMap;

import static ru.volod878.english.bot.command.CommandName.*;

@Component
public class EnglishBot extends TelegramLongPollingBot {
    @Value("${EnglishBot.username}")
    private String username;
    @Value("${EnglishBot.token}")
    private String token;

    private final CommandContainer commandContainer;

    @Autowired
    public EnglishBot(IVocabularyService vocabularyService) {
        ISendBotMessage sendBotMessage = new SendBotMessage(this);
        ConcurrentHashMap<String, Command> commandMap = new ConcurrentHashMap<>();
        commandMap.put(EXAMINATION.getCommandName(), new ExaminationCommand(sendBotMessage));
        commandMap.put(START.getCommandName(), new StartCommand(sendBotMessage));
        commandMap.put(WORD_INFO.getCommandName(), new WordInfoCommand(sendBotMessage));
        this.commandContainer = new CommandContainer(sendBotMessage, commandMap, vocabularyService);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (isMessageWithText(update)) {
            String message = update.getMessage().getText().trim();
            commandContainer.retrieveCommand(message).execute(update);
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    private boolean isMessageWithText(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
    }
}