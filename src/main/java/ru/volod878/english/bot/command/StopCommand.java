package ru.volod878.english.bot.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.volod878.english.domain.model.User;
import ru.volod878.english.domain.repository.UserRepository;

import java.util.Objects;

import static ru.volod878.english.bot.command.CommandName.STOP;

@Slf4j
@RequiredArgsConstructor
public class StopCommand implements Command {
    private final UserRepository userRepository;

    @Override
    public void execute(Update update) {
        log.info("the {} command is executed", STOP);
        Long telegramUserId = Objects.nonNull(update.getMyChatMember()) ?
                update.getMyChatMember().getFrom().getId() :
                update.getMessage().getFrom().getId();
        User user = userRepository.findByTelegramUserId(telegramUserId);
        user.setActive(false);
        user.setActiveCommand(null);
        userRepository.save(user);
    }
}