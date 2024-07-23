package com.twentyone.steachserver.domain.curriculum.enums;

import java.util.List;

public enum CurriculumCategory {
    KOREAN, MATH, FOREIGN_LANGUAGE, SCIENCE, ENGINEERING, ARTS_AND_PHYSICAL, EDUCATION, ETC;

    public static int size() {
        return CurriculumCategory.values().length;
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
