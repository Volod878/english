package ru.volod878.english.bot.command;

import com.google.common.collect.ImmutableMap;
import ru.volod878.english.service.ISendBotMessage;
import ru.volod878.english.service.IVocabularyService;

import java.util.concurrent.ConcurrentHashMap;

public class CommandContainer {
    private final ImmutableMap<String, Command> commandMap;
    private final Command getWordCommand;

    public CommandContainer(ISendBotMessage sendBotMessage, ConcurrentHashMap<String, Command> commandMap, IVocabularyService vocabularyService) {
        this.commandMap = ImmutableMap.<String, Command>builder().putAll(commandMap).build();
        getWordCommand = new GetWordCommand(sendBotMessage, vocabularyService);
    }

    public Command retrieveCommand(String commandIdentifier) {
        return commandMap.getOrDefault(commandIdentifier, getWordCommand);
    }
}