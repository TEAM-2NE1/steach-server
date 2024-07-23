package com.twentyone.steachserver.domain.curriculum.validator;

import com.twentyone.steachserver.domain.curriculum.dto.CurriculumAddRequest;
import org.springframework.stereotype.Component;

@Component
public class CurriculumValidator {

    public void validatorMaxAttendees(CurriculumAddRequest request) {
        if ( request.getMaxAttendees() < 1 || 4 < request.getMaxAttendees()  ) {
            throw new IllegalArgumentException("최대 정원은 1이상 4이하 여야합니다.");
        }
    }
}
