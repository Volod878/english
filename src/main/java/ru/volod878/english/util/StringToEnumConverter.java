package ru.volod878.english.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.volod878.english.domain.enums.Location;

@Component
public class StringToEnumConverter implements Converter<String, Location> {
    @Override
    public Location convert(String source) {
        return Location.valueOf(source.toUpperCase());
    }
}