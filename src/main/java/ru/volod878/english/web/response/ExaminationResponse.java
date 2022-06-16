package ru.volod878.english.web.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.volod878.english.web.dto.ExaminationDTO;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExaminationResponse {
    private String result;
    private List<ExaminationDTO> right;
    private List<ExaminationDTO> wrong;

    @Override
    public String toString() {
        return result + "\n\n" +
                "<b>Правильные ответы</b>\n" +
                right.stream().map(dto -> dto.toString() + '\n').collect(Collectors.joining()) + '\n' +
                "<b>Неправильные ответы</b>\n" +
                wrong.stream().map(dto -> dto.toString() + '\n').collect(Collectors.joining());
    }
}