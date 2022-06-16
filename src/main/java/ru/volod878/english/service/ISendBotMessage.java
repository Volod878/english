package ru.volod878.english.service;

import ru.volod878.english.bot.command.Command;

import java.io.File;

public interface ISendBotMessage {
    void sendMessage(String chatId, String message);

    void sendVoice(String chatId, File voice);

    void setActiveCommand(Command command);
}