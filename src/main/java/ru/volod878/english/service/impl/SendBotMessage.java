package ru.volod878.english.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVoice;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.volod878.english.bot.EnglishBot;
import ru.volod878.english.service.ISendBotMessage;

import java.io.File;

@Slf4j
@Service
public class SendBotMessage implements ISendBotMessage {

    private final EnglishBot bot;

    public SendBotMessage(EnglishBot bot) {
        this.bot = bot;
    }

    @Override
    public void sendMessage(String chatId, String message) {
        log.info("sending message in chat with id = {}", chatId);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableHtml(true);
        sendMessage.setText(message);
        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendVoice(String chatId, File voice) {
        SendVoice sendVoice = new SendVoice();
        sendVoice.setChatId(chatId);
        sendVoice.setVoice(new InputFile(voice));
        try {
            bot.execute(sendVoice);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}