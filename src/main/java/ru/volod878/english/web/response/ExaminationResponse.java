package ru.volod878.english.web.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExaminationResponse {
    private String result;
    private List<ExaminationDTO> right;
    private List<ExaminationDTO> wrong;
}