package ru.volod878.english.bot;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.volod878.english.bot.command.*;
import ru.volod878.english.domain.model.User;
import ru.volod878.english.domain.repository.UserRepository;
import ru.volod878.english.service.ILearningService;
import ru.volod878.english.service.ISendBotMessage;
import ru.volod878.english.service.IVocabularyService;
import ru.volod878.english.service.impl.SendBotMessage;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static ru.volod878.english.bot.command.CommandName.*;

@Getter
@Component
public class EnglishBot extends TelegramLongPollingBot {
    @Value("${EnglishBot.username}")
    private String username;
    @Value("${EnglishBot.token}")
    private String token;

    private final CommandContainer commandContainer;
    private final UserRepository userRepository;

    @Autowired
    public EnglishBot(IVocabularyService vocabularyService, ILearningService learningService, UserRepository userRepository) {
        ISendBotMessage sendBotMessage = new SendBotMessage(this);
        ConcurrentHashMap<String, Command> commandMap = new ConcurrentHashMap<>();
        commandMap.put(EXAMINATION.getCommandName(), new ExaminationCommand(sendBotMessage, learningService, userRepository));
        commandMap.put(START.getCommandName(), new StartCommand(sendBotMessage, userRepository));
        commandMap.put(WORD_INFO.getCommandName(), new WordInfoCommand(sendBotMessage));
        commandMap.put(STOP.getCommandName(), new StopCommand(userRepository));
        this.commandContainer = new CommandContainer(sendBotMessage, commandMap, vocabularyService);
        this.userRepository = userRepository;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (isFirstStart(update)) {
            commandContainer.retrieveCommand(START.getCommandName()).execute(update);
        } else if (needStopUser(update)) {
            commandContainer.retrieveCommand(STOP.getCommandName()).execute(update);
        } else if (isActiveUserMessage(update)) {
            Long telegramUserId = update.getMessage().getFrom().getId();
            User user = userRepository.findByTelegramUserId(telegramUserId);
            String message = update.getMessage().getText().trim();
            if (Objects.nonNull(user.getActiveCommand()) && (message.charAt(0) != '/')) {
                commandContainer.retrieveCommand(user.getActiveCommand().getCommandName()).execute(update);
            } else {
                user.setActiveCommand(null);
                userRepository.save(user);
                commandContainer.retrieveCommand(message).execute(update);
            }
        }
    }

    private boolean isFirstStart(Update update) {
        if (update.hasMessage()) {
            User user = userRepository.findByTelegramUserId(update.getMessage().getFrom().getId());
            return Objects.isNull(user);
        }
        return false;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    private boolean isActiveUserMessage(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            User user = userRepository.findByTelegramUserId(update.getMessage().getFrom().getId());
            String message = update.getMessage().getText().trim();
            return user.isActive() || START.getCommandName().equals(message);
        }
        return false;
    }

    private boolean needStopUser(Update update) {
        return update.hasMyChatMember() &&
                update.getMyChatMember().getNewChatMember().getStatus().equals("kicked")
                ||
                update.hasMessage() && update.getMessage().hasText() &&
                        update.getMessage().getText().equals(STOP.getCommandName());
    }
}