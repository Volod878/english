package ru.volod878.english.bot.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.volod878.english.service.ILearningService;
import ru.volod878.english.service.ISendBotMessage;
import ru.volod878.english.web.response.ExaminationResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.util.Collections.emptyList;
import static ru.volod878.english.bot.command.CommandName.EXAMINATION;

@Slf4j
@RequiredArgsConstructor
public class ExaminationCommand implements Command {
    private final ISendBotMessage sendBotMessage;
    private final ILearningService learningService;
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
            sendBotMessage.setActiveCommand(this);
            sendBotMessage.sendMessage(update.getMessage().getChatId().toString(),
                    String.format(EXAMINATION_MESSAGE, DEFAULT_LIMIT));
            fewWords = learningService.getFewWords(DEFAULT_LIMIT);
        } else {
            answers.put(fewWords.get(step - 1), update.getMessage().getText().toLowerCase(Locale.ROOT).trim());
        }
        if (step < fewWords.size()) {
            String word = fewWords.get(step++);
            sendBotMessage.sendMessage(update.getMessage().getChatId().toString(), word);
        } else {
            log.info("the {} command is executed. step = {}, answers = {}", EXAMINATION, step, answers);
            ExaminationResponse examination = learningService.examination(answers);
            step = 0;
            fewWords = emptyList();
            answers = new HashMap<>();
            sendBotMessage.setActiveCommand(null);
            sendBotMessage.sendMessage(update.getMessage().getChatId().toString(), examination.toString());
        }
    }
}