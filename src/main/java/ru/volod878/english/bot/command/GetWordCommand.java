package ru.volod878.english.bot.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.volod878.english.domain.enums.Location;
import ru.volod878.english.service.ISendBotMessage;
import ru.volod878.english.service.IVocabularyService;
import ru.volod878.english.web.dto.VocabularyDto;

@Slf4j
@RequiredArgsConstructor
public class GetWordCommand implements Command {
    private final ISendBotMessage sendBotMessage;
    private final IVocabularyService vocabularyService;

    /**
     * Регулярное выражение:
     * Строка должна начинаться и заканчиваться символом англ алфавита
     * Строка должна содержать только одно слово
     */
    private final static String ONLY_TEXT_PATTERN = "^[a-zA-Z]+$";

    @Override
    public void execute(Update update) {
        if (update.getMessage().getText().matches(ONLY_TEXT_PATTERN)) {
            log.info("the GET_WORD command is executed");
            VocabularyDto dto = vocabularyService.getOrCreateByWord(update.getMessage().getText());
            String chatId = update.getMessage().getChatId().toString();
            sendBotMessage.sendMessage(chatId, dto.toString());
            sendBotMessage.sendVoice(chatId, vocabularyService.getMp3File(Location.US, dto.getWord()));
            sendBotMessage.sendVoice(chatId, vocabularyService.getMp3File(Location.UK, dto.getWord()));
        }
    }
}