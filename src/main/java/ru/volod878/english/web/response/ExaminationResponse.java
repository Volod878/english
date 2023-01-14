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
        StringBuilder stringBuilder = new StringBuilder(result).append("\n\n");
        if (!right.isEmpty()) {
            stringBuilder.append("<b>Правильные ответы</b>\n")
                    .append(right.stream().map(dto -> dto.toString() + '\n').collect(Collectors.joining()))
                    .append('\n');
        }
        if (!wrong.isEmpty()) {
            stringBuilder.append("<b>Неправильные ответы</b>\n")
                    .append(wrong.stream().map(dto -> dto.toString() + '\n').collect(Collectors.joining()));
        }
        return stringBuilder.toString();
    }
}