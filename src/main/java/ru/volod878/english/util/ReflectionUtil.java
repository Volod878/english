package ru.volod878.english.util;

import ru.volod878.english.domain.model.Vocabulary;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectionUtil {
    public static <Model> String getFieldValue(String columnName, Model model) {
        try {
            String methodName = "get" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
            Method getFieldValue = Vocabulary.class.getMethod(methodName);
            Object value = getFieldValue.invoke(model);
            if (value instanceof Integer) {
                return String.valueOf(value);
            } else {
                return (String) value;
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            //TODO другое исключение
            throw new RuntimeException(e);
        }
    }

    public static <Model> String[] getFieldNames(Class<Model> modelClass) {
        return Arrays.stream(modelClass.getDeclaredFields())
                .map(Field::getName)
                .toArray(String[]::new);
    }
}
