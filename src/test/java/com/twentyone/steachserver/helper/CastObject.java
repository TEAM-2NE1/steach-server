package com.twentyone.steachserver.helper;

import java.util.List;

public class CastObject {
    @SuppressWarnings("unchecked")
    public static <T> List<T> castList(Object obj, Class<T> clazz) {
        if (obj instanceof List<?>) {
            List<?> list = (List<?>) obj;
            if (list.isEmpty() || clazz.isInstance(list.get(0))) {
                return (List<T>) list;
            }
        }
        throw new ClassCastException("Failed to cast object to List<" + clazz.getName() + ">");
    }
}
