package com.twentyone.steachserver.domain.curriculum.enums;

import java.util.List;

public enum CurriculumCategory {
    // ETC는 항상 뒤에 해주세요!
    KOREAN, MATH, FOREIGN_LANGUAGE, SCIENCE, ENGINEERING, ARTS_AND_PHYSICAL, SOCIAL, ETC;

    public static int size() {
        return CurriculumCategory.values().length;
    }

    public static int sizeExcludingETC() {
        return CurriculumCategory.values().length - 1;
    }

    public static List<CurriculumCategory> getCategories() {
        return List.of(CurriculumCategory.values());
    }

    public static CurriculumCategory getCategoryByIndex(int index) {
        CurriculumCategory[] categories = CurriculumCategory.values();
        if (index < 0 || index >= categories.length) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        return categories[index];
    }
}
