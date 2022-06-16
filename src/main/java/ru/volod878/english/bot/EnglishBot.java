package ru.volod878.english.bot;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.volod878.english.bot.command.*;
import ru.volod878.english.service.ILearningService;
import ru.volod878.english.service.ISendBotMessage;
import ru.volod878.english.service.IVocabularyService;
import ru.volod878.english.service.SendBotMessage;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static ru.volod878.english.bot.command.CommandName.*;

@Component
public class EnglishBot extends TelegramLongPollingBot {
    @Value("${EnglishBot.username}")
    private String username;
    @Value("${EnglishBot.token}")
    private String token;

    private final CommandContainer commandContainer;

    @Getter
    @Setter
    private Command activeCommand;

    @Autowired
    public EnglishBot(IVocabularyService vocabularyService, ILearningService learningService) {
        ISendBotMessage sendBotMessage = new SendBotMessage(this);
        ConcurrentHashMap<String, Command> commandMap = new ConcurrentHashMap<>();
        commandMap.put(EXAMINATION.getCommandName(), new ExaminationCommand(sendBotMessage, learningService));
        commandMap.put(START.getCommandName(), new StartCommand(sendBotMessage));
        commandMap.put(WORD_INFO.getCommandName(), new WordInfoCommand(sendBotMessage));
        this.commandContainer = new CommandContainer(sendBotMessage, commandMap, vocabularyService);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (isMessageWithText(update)) {
            if (Objects.nonNull(activeCommand)) {
                activeCommand.execute(update);
            } else {
                String message = update.getMessage().getText().trim();
                commandContainer.retrieveCommand(message).execute(update);
            }
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