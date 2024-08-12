package com.twentyone.steachserver.domain.curriculum.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum CurriculumCategory {
    // ETC는 항상 뒤에 해주세요!
    KOREAN("국어", "Korean"), // 한국어 관련 과목
    MATH("수학", "Math"), // 수학 관련 과목
    FOREIGN_LANGUAGE("외국어", "Foreign_language"), // 외국어 관련 과목
    SCIENCE("과학", "Science"), // 과학 관련 과목
    ENGINEERING("공학", "Engineering"), // 공학 관련 과목
    ARTS_AND_PHYSICAL("예체능", "Arts_And_Physical"), // 예술 및 체육 관련 과목
    SOCIAL("사회", "Social"), // 사회과학 관련 과목
    ETC("기타", "ETC"); // 기타 과목

    private final String korean;
    private final String english;

    CurriculumCategory(String korean, String english) {
        this.korean = korean;
        this.english = english;
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
                .map(CurriculumCategory::getEnglish)
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
