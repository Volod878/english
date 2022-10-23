package ru.volod878.english.bot.command;

public enum CommandName {
    EXAMINATION("/examination"),
    START("/start"),
    WORD_INFO("/word_info"),
    STOP("/stop");

    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}