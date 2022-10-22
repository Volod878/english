package ru.volod878.english.bot.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.volod878.english.domain.model.User;
import ru.volod878.english.domain.repository.UserRepository;
import ru.volod878.english.service.ILearningService;
import ru.volod878.english.service.ISendBotMessage;
import ru.volod878.english.web.response.ExaminationResponse;

import java.util.*;

import static java.util.Collections.emptyList;
import static ru.volod878.english.bot.command.CommandName.EXAMINATION;

@Slf4j
@RequiredArgsConstructor
public class ExaminationCommand implements Command {
    private final ISendBotMessage sendBotMessage;
    private final ILearningService learningService;
    private final UserRepository userRepository;
    public static final String EXAMINATION_MESSAGE =
            "Вам будет предоставлено %s слов которые нужно перевести.\n" +
                    "После каждого слова отправьте в ответ перевод.\n" +
                    "Если походящих ответов несколько - пишите через запятую";
    private static final int DEFAULT_LIMIT = 5;
    private int step;
    private List<String> fewWords = emptyList();
    private Map<String, String> answers = new HashMap<>();

    @Override
    public void execute(Update update) {
        log.info("the {} command is execute. step = {}", EXAMINATION, step);
        if (step == 0) {
            startExam(update.getMessage().getChatId().toString());
        } else {
            answers.put(fewWords.get(step - 1), update.getMessage().getText().toLowerCase(Locale.ROOT).trim());
            if (step < fewWords.size()) {
                String word = fewWords.get(step++);
                sendBotMessage.sendMessage(update.getMessage().getChatId().toString(), word);
            } else {
                log.info("the {} command is executed. step = {}, answers = {}", EXAMINATION, step, answers);
                User user = userRepository.findByTelegramUserId(update.getMessage().getFrom().getId());
                ExaminationResponse examination = learningService.examination(answers, user);
                step = 0;
                fewWords = emptyList();
                answers = new HashMap<>();
                sendBotMessage.setActiveCommand(null);
                sendBotMessage.sendMessage(update.getMessage().getChatId().toString(), examination.toString());
            }
        }
    }

    public void startExam(String chatId) {
        sendBotMessage.setActiveCommand(this);
        sendBotMessage.sendMessage(chatId, String.format(EXAMINATION_MESSAGE, DEFAULT_LIMIT));
        fewWords = learningService.getFewWords(DEFAULT_LIMIT);
        String word = fewWords.get(step++);
        sendBotMessage.sendMessage(chatId, word);
    }
}