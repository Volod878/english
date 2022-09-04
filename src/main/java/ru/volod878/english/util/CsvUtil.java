package ru.volod878.english.util;

import liquibase.util.csv.CSVReader;
import liquibase.util.csv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.volod878.english.exception.CsvException;

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
    private static final String VERIFY_ERROR_MESSAGE =
            "Обнаружено не соответствие имени поля из файла с именем поля из объекта";
    private static final String CREATE_NEW_MODEL_ERROR_MESSAGE = "Неудачная попытка создания объекта";

    public static <Model> List<Model> fromCsv(File file, Class<Model> modelClass, char spliterator) throws IOException {
        try (CSVReader reader = new CSVReader(new FileReader(file), spliterator)) {
            String[] fieldNames = getFieldNames(modelClass);
            verifyField(fieldNames, Arrays.asList(reader.readNext()));
            return reader.readAll().stream()
                    .map(row -> createNewModel(fieldNames, row, modelClass))
                    .collect(Collectors.toList());
        }
    }

    public static <Model> void createCsv(File file, List<Model> models, char spliterator) throws IOException {
        if (models.size() == 0) {
            return;
        }
        try (CSVWriter writer = new CSVWriter(new FileWriter(file), spliterator)) {
            String[] fieldNames = getFieldNames(models.get(0).getClass());
            writer.writeNext(fieldNames);
            toCsv(writer, spliterator, models, fieldNames);
        }
    }

    public static <Model> void updateCsv(File file, List<Model> models, char spliterator) throws IOException {
        if (models.size() == 0) {
            return;
        }
        Class<?> modelClass = models.get(0).getClass();
        try (CSVReader reader = new CSVReader(new FileReader(file), spliterator)) {
            verifyField(getFieldNames(modelClass), Arrays.asList(reader.readNext()));
        }
        try (CSVWriter writer = new CSVWriter(new FileWriter(file, true), spliterator)) {
            String[] fieldNames = getFieldNames(modelClass);
            toCsv(writer, spliterator, models, fieldNames);
        }
    }

    private static <Model> void toCsv(CSVWriter writer, char spliterator, List<Model> models, String[] fieldNames) {
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

    /**
     * @param expectFieldNames Все ожидаемые поля должны присутствовать в actualFieldNames
     * @param actualFieldNames Фактические поля в файле csv. Их может быть больше чем в expectFieldNames
     */
    private static void verifyField(String[] expectFieldNames, List<String> actualFieldNames) {
        boolean matched = Arrays.stream(expectFieldNames).allMatch(actualFieldNames::contains);
        if (!matched) {
            log.error(VERIFY_ERROR_MESSAGE);
            throw new CsvException(VERIFY_ERROR_MESSAGE);
        }
    }

    private static <Model> Model createNewModel(String[] fieldNames, String[] fieldValues, Class<Model> modelClass) {
        try {
            Model model = modelClass.newInstance();
            for (int i = 0; i < fieldNames.length; i++) {
                setFieldValue(fieldNames[i], fieldValues[i], model);
            }
            return model;
        } catch (InstantiationException | IllegalAccessException exception) {
            log.error(CREATE_NEW_MODEL_ERROR_MESSAGE, exception);
            throw new CsvException(exception);
        }
    }


}
