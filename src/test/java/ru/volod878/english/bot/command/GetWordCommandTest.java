package ru.volod878.english.bot.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.volod878.english.service.ISendBotMessage;
import ru.volod878.english.service.IVocabularyService;
import ru.volod878.english.web.dto.VocabularyDto;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetWordCommandTest {
    @Mock
    private ISendBotMessage sendBotMessage;
    @Mock
    private IVocabularyService vocabularyService;
    private GetWordCommand getWordCommand;
    private VocabularyDto vocabularyDto;

    @BeforeEach
    public void setUp() {
        getWordCommand = new GetWordCommand(sendBotMessage, vocabularyService);
        vocabularyDto = new VocabularyDto();
    }

    private static final String ENG_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String RUS_ALPHABET =
            "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеежзийклмнопрстуфхцчшщъыьэюя";
    private static final char[] OTHER_SYMBOLS = "! @#$%^&*()_+=-/*-+[]\\{}|;'\":.,<>?`~".toCharArray();

    @Test
    @DisplayName("Method execute() should send message")
    void shouldSendMessage() {
        when(vocabularyService.getOrCreateByWord(ENG_ALPHABET)).thenReturn(vocabularyDto);
        Update update = prepareUpdateMessage(ENG_ALPHABET);
        getWordCommand.execute(update);
        verify(sendBotMessage).sendMessage(update.getMessage()
                .getChatId().toString(), vocabularyDto.toString());
    }

    @Test
    @DisplayName("Method execute() should send voice two times")
    void shouldSendVoiceTwoTimes() {
        when(vocabularyService.getOrCreateByWord(ENG_ALPHABET)).thenReturn(vocabularyDto);
        Update update = prepareUpdateMessage(ENG_ALPHABET);
        getWordCommand.execute(update);
        verify(sendBotMessage, times(2))
                .sendVoice(update.getMessage().getChatId().toString(), null);
    }

    @Test
    @DisplayName("Method execute(). Message should contains only eng alphabet")
    void shouldContainsOnlyEngAlphabet() {
        when(vocabularyService.getOrCreateByWord(ENG_ALPHABET)).thenReturn(vocabularyDto);
        Update update = prepareUpdateMessage(ENG_ALPHABET);
        getWordCommand.execute(update);
        verify(sendBotMessage).sendMessage(update.getMessage()
                .getChatId().toString(), vocabularyDto.toString());
    }

    @Disabled
    @Test
    @DisplayName("Method execute(). Message should contains only rus alphabet")
    void shouldContainsOnlyRusAlphabet() {
        // Еще не реализовано
        when(vocabularyService.getOrCreateByWord(RUS_ALPHABET)).thenReturn(vocabularyDto);
        Update update = prepareUpdateMessage(RUS_ALPHABET);
        getWordCommand.execute(update);
        verify(sendBotMessage).sendMessage(update.getMessage()
                .getChatId().toString(), vocabularyDto.toString());
    }

    @Test
    @DisplayName("Method execute(). Message not should contains other symbols")
    void shouldNotContainsOtherSymbols() {
        for (char ch : OTHER_SYMBOLS) {
            Update update = prepareUpdateMessage(String.valueOf(ch));
            getWordCommand.execute(update);
            verify(sendBotMessage, never())
                    .sendMessage(update.getMessage().getChatId().toString(), vocabularyDto.toString());
        }
    }

    @Test
    @DisplayName("Method execute(). Message not should contains mixed symbols")
    void shouldNotContainsMixedSymbols() {
        Update update = prepareUpdateMessage("словоword");
        getWordCommand.execute(update);
        verify(sendBotMessage, never())
                .sendMessage(update.getMessage().getChatId().toString(), vocabularyDto.toString());
    }

    private static Update prepareUpdateMessage(String word) {
        Update update = new Update();
        Message message = mock(Message.class);
        when(message.getChatId()).thenReturn(1L);
        when(message.getText()).thenReturn(word);
        update.setMessage(message);
        return update;
    }
}