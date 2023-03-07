package ru.volod878.english.bot.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.volod878.english.domain.model.User;
import ru.volod878.english.domain.model.WordLearning;
import ru.volod878.english.domain.repository.UserRepository;
import ru.volod878.english.service.ILearningService;
import ru.volod878.english.service.ISendBotMessage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import static ru.volod878.english.bot.command.CommandName.STAT;

@Slf4j
@RequiredArgsConstructor
public class StatCommand implements Command {
    private final ISendBotMessage sendBotMessage;
    private final ILearningService learningService;
    private final UserRepository userRepository;
    public final static String STAT_MESSAGE = "Статистика по экзаменам:\n" +
            "Вам задали слов - %d\n" +
            "Ответили правильно - %d\n" +
            "Не правильно - %d\n" +
            "\n" +
            "По результатам всех экзаменов:\n" +
            "Было уникальных слов - %d\n" +
            "Из них вы знаете - %d\n" +
            "Не знаете - %d";

    @Override
    public void execute(Update update) {
        log.info("the {} command is executed", STAT);
        User user = userRepository.findByTelegramUserId(update.getMessage().getFrom().getId());
        List<WordLearning> wordLearnings = learningService.info(user);
        int allAnswer = wordLearnings.size();
        int rightAnswer = (int) wordLearnings.stream().filter(WordLearning::isAnswerIsRight).count();
        int wrongAnswer = allAnswer - rightAnswer;

        Collection<WordLearning> uniqueWordLearnings = learningService.info(user).stream()
                .collect(Collectors.toMap(WordLearning::getVocabulary,
                        wordLearning -> wordLearning,
                        BinaryOperator.maxBy(Comparator.comparing(WordLearning::getInsTime)))
                ).values();
        int allWords = uniqueWordLearnings.size();
        int knowWords = (int) uniqueWordLearnings.stream().filter(WordLearning::isAnswerIsRight).count();
        int dontKnowWords = allWords - knowWords;

        String callbackMessage = String.format(STAT_MESSAGE,
                allAnswer, rightAnswer, wrongAnswer, allWords, knowWords, dontKnowWords);
        String chatId = update.getMessage().getChatId().toString();
        sendBotMessage.sendMessage(chatId, callbackMessage);
    }
}