package com.twentyone.steachserver.helper;

import java.util.List;

public class CastObject {
    /**
     * @SuppressWarnings("unchecked") 애노테이션은 컴파일러에게 경고를 무시하도록 지시하는 역할을 합니다.
     * 구체적으로 "unchecked" 경고는 제네릭 타입 변환과 관련된 경고를 의미합니다.
     * 예를 들어, 제네릭 타입을 사용할 때 타입 안전성을 보장하지 않는 상황에서 발생하는 경고를 무시할 수 있습니다.
     */
    @SuppressWarnings("unchecked")
    // 이 메서드는 객체를 특정 클래스의 리스트로 캐스팅하는 기능을 제공합니다.
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
