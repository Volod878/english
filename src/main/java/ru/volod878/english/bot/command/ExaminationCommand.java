package ru.volod878.english.bot.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.volod878.english.domain.model.User;
import ru.volod878.english.domain.repository.UserRepository;
import ru.volod878.english.service.ILearningService;
import ru.volod878.english.service.ISendBotMessage;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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

    private static final Map<User, Exam> exams = new ConcurrentHashMap<>();

    @Override
    public void execute(Update update) {
        User user = userRepository.findByTelegramUserId(update.getMessage().getFrom().getId());
        String message = update.getMessage().getText().trim();
        if ((message.charAt(0) == '/') || !exams.containsKey(user)) {
            exams.put(user, new Exam());
            startExam(user);
        } else {
            Exam exam = exams.get(user);
            exam.saveAnswer(update.getMessage().getText());
            String responseMessage = exam.hasWord() ? exam.nextWord() : finishExam(user);
            sendBotMessage.sendMessage(update.getMessage().getChatId().toString(), responseMessage);
        }
    }

    public void startExam(User user) {
        sendBotMessage.sendMessage(String.valueOf(user.getTelegramUserId()),
                String.format(EXAMINATION_MESSAGE, DEFAULT_LIMIT));
        String word = exams.get(user).start(learningService.getFewWords(DEFAULT_LIMIT));
        sendBotMessage.sendMessage(String.valueOf(user.getTelegramUserId()), word);
        user.setActiveCommand(EXAMINATION);
        userRepository.save(user);
    }

    private String finishExam(User user) {
        user.setActiveCommand(null);
        userRepository.save(user);
        return learningService.examination(exams.get(user).result(), user).toString();
    }

    private static class Exam {
        private int step;
        private List<String> fewWords = emptyList();
        private Map<String, String> answers = new HashMap<>();

        private String start(List<String> words) {
            log.info("the {} command is execute", EXAMINATION);
            fewWords = words;
            return fewWords.get(step++);
        }

        private void saveAnswer(String answer) {
            answers.put(fewWords.get(step - 1), answer.toLowerCase(Locale.ROOT).trim());
        }

        private boolean hasWord() {
            return step < fewWords.size();
        }

        private String nextWord() {
            log.info("the {} command is execute. step = {}", EXAMINATION, step);
            return fewWords.get(step++);
        }

        public Map<String, String> result() {
            log.info("the {} command completed. step = {}, answers = {}", EXAMINATION, step, answers);
            step = 0;
            fewWords = emptyList();
            Map<String, String> result = new HashMap<>(answers);
            answers = new HashMap<>();
            return result;
        }
    }
}