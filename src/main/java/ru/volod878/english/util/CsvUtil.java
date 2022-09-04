package ru.volod878.english.util;

import liquibase.util.csv.CSVReader;
import liquibase.util.csv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ru.volod878.english.util.ReflectionUtil.*;

public class CsvUtil {
    private static final Logger log = LoggerFactory.getLogger(CsvUtil.class);

    public static <Model> List<Model> fromCsv(File file, Class<Model> modelClass, char spliterator) throws IOException {
        try (CSVReader reader = new CSVReader(new FileReader(file), spliterator)) {
            String[] fieldNames = getFieldNames(modelClass);
            verifyField(fieldNames, Arrays.asList(reader.readNext()));
            return reader.readAll().stream()
                    .map(row -> createNewModel(fieldNames, row, modelClass))
                    .collect(Collectors.toList());
        }
    }

    public static <Model> void toCsv(File file, List<Model> models, char spliterator) throws IOException {
        if (models.size() == 0) {
            return;
        }
        try (CSVWriter writer = new CSVWriter(new FileWriter(file), spliterator)) {
            String[] fieldNames = getFieldNames(models.get(0).getClass());
            writer.writeNext(fieldNames);
            models.forEach(model -> {
                String[] row = new String[fieldNames.length];
                for (int i = 0; i < fieldNames.length; i++) {
                    row[i] = getFieldValue(fieldNames[i], model);
                    if (row[i].contains(String.valueOf(spliterator))) {
                        log.error("В записи присутствует знак ';'. {}", model);
                    }
                }
                writer.writeNext(row);
            });
        }
    }

    /**
     * @param expectFieldNames Все ожидаемые поля должны присутствовать в actualFieldNames
     * @param actualFieldNames Фактические поля в файле csv. Их может быть больше чем в expectFieldNames
     */
    private static void verifyField(String[] expectFieldNames, List<String> actualFieldNames) throws IOException {
        boolean matched = Arrays.stream(expectFieldNames).allMatch(actualFieldNames::contains);
        if (!matched) {
            //TODO другое исключение
            throw new IOException();
        }
    }

    private static <Model> Model createNewModel(String[] fieldNames, String[] fieldValues, Class<Model> modelClass) {
        try {
            Model model = modelClass.newInstance();
            for (int i = 0; i < fieldNames.length; i++) {
                setFieldValue(fieldNames[i], fieldValues[i], model);
            }
            return model;
        } catch (InstantiationException | IllegalAccessException e) {
            //TODO другое исключение
            throw new RuntimeException(e);
        }
    }


}
