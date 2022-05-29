package ru.volod878.english.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.volod878.english.bot.EnglishBot;

@Slf4j
@Service
public class SendBotMessage implements ISendBotMessage {

    private final EnglishBot bot;
    private final SendMessage sendMessage = new SendMessage();

    public SendBotMessage(EnglishBot bot) {
        this.bot = bot;
    }

    @Override
    public void sendMessage(String chatId, String message) {
        log.info("sending message in chat with id = {}", chatId);

        sendMessage.setChatId(chatId);
        sendMessage.enableHtml(true);
        sendMessage.setText(message);
        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}