package com.utils;

import lombok.SneakyThrows;

import java.util.List;
import java.util.Random;

public class EnumUtils {

    private static final Random RANDOM = new Random();

    @SneakyThrows
    public static <T extends Enum<T>> T getRandomEnumValue(Class<T> myEnum){

        List<T> values = List.of(myEnum.getEnumConstants());
        int size = values.size();

        return values.get(RANDOM.nextInt(size));
    }
}
