package ru.volod878.english.bot.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.volod878.english.domain.model.User;
import ru.volod878.english.domain.repository.UserRepository;
import ru.volod878.english.service.ISendBotMessage;

import java.util.Objects;

import static ru.volod878.english.bot.command.CommandName.START;

@Slf4j
@RequiredArgsConstructor
public class StartCommand implements Command {
    private final ISendBotMessage sendBotMessage;
    private final UserRepository userRepository;

    public final static String START_MESSAGE = "Привет!\n" +
            "Я помогу тебе выучить английские слова.\n" +
            "Напиши мне любое английское слово и я постараюсь его перевести.";

    @Override
    public void execute(Update update) {
        log.info("the {} command is executed", START);
        User user = userRepository.findByTelegramUserId(update.getMessage().getFrom().getId());
        if (Objects.isNull(user)) {
            log.info("Добавление нового пользователя: {}", update.getMessage().getFrom().getId());
            user = new User(update.getMessage().getFrom());
        } else {
            user.setActive(true);
        }
        userRepository.save(user);
        sendBotMessage.sendMessage(update.getMessage().getChatId().toString(), START_MESSAGE);
    }
}