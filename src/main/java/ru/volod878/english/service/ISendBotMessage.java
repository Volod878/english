package ru.volod878.english.service;

import java.io.File;

public interface ISendBotMessage {
    void sendMessage(String chatId, String message);

    void sendVoice(String chatId, File voice);
}