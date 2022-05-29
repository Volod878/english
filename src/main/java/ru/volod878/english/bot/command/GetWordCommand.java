package ru.volod878.english.bot.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.volod878.english.service.ISendBotMessage;
import ru.volod878.english.service.IVocabularyService;
import ru.volod878.english.web.dto.VocabularyDto;

@Slf4j
@RequiredArgsConstructor
public class GetWordCommand implements Command {
    private final ISendBotMessage sendBotMessage;
    private final IVocabularyService vocabularyService;

    @Override
    public void execute(Update update) {
        log.info("the GET_WORD command is executed");
        VocabularyDto dto = vocabularyService.getOrCreateByWord(update.getMessage().getText());
        sendBotMessage.sendMessage(update.getMessage().getChatId().toString(), dto.toString());
    }
}