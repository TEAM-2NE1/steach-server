package com.twentyone.steachserver.domain.curriculum.enums;

import java.util.Arrays;
import java.util.List;

public enum CurriculumCategory {
    // ETC는 항상 뒤에 해주세요!
    KOREAN("국어"), // 한국어 관련 과목
    MATH("수학"), // 수학 관련 과목
    FOREIGN_LANGUAGE("외국어"), // 외국어 관련 과목
    SCIENCE("과학"), // 과학 관련 과목
    ENGINEERING("공학"), // 공학 관련 과목
    ARTS_AND_PHYSICAL("예체능"), // 예술 및 체육 관련 과목
    SOCIAL("사회"), // 사회과학 관련 과목
    ETC("기타"); // 기타 과목

    private final String description;

    CurriculumCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static int size() {
        return CurriculumCategory.values().length;
    }

    public static int sizeExcludingETC() {
        return CurriculumCategory.values().length - 1;
    }

    public static List<CurriculumCategory> getCategories() {
        return List.of(CurriculumCategory.values());
    }

    public static List<String> getCategoriesDescription() {
        return Arrays.stream(CurriculumCategory.values())
                .limit(CurriculumCategory.values().length - 1)
                .map(CurriculumCategory::getDescription)
                .toList();

    }


    public static CurriculumCategory getCategoryByIndex(int index) {
        CurriculumCategory[] categories = CurriculumCategory.values();
        if (index < 0 || index >= categories.length) {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        return categories[index];
    }
}
