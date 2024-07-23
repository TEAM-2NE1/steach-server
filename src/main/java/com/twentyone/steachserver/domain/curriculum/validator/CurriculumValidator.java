package com.twentyone.steachserver.domain.curriculum.validator;

import com.twentyone.steachserver.domain.curriculum.dto.CurriculumAddRequest;
import org.springframework.stereotype.Component;

@Component
public class CurriculumValidator {

    public void validatorMaxAttendees(CurriculumAddRequest request) {
        if (1 <= request.getMaxAttendees() && request.getMaxAttendees() <= 4 ) {
            throw new IllegalArgumentException("최대 정원은 1이상 4이하 여야합니다.");
        }
    }
}
