package com.twentyone.steachserver.domain.member.memberController;

import com.twentyone.steachserver.domain.curriculum.dto.CurriculumListResponse;
import com.twentyone.steachserver.domain.curriculum.service.CurriculumService;
import com.twentyone.steachserver.domain.member.dto.TeacherInfoRequest;
import com.twentyone.steachserver.domain.member.dto.TeacherInfoResponse;
import com.twentyone.steachserver.domain.member.model.Teacher;
import com.twentyone.steachserver.domain.member.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "강사")
@Secured("ROLE_TEACHER")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/teachers")
public class TeacherController {
    private final TeacherService teacherService;
    private final CurriculumService curriculumService;

    @Operation(summary = "[강사] 회원정보 조회", description = "brief_introduction, academic_background, specialization은 초기에 null임")
    @GetMapping
    public ResponseEntity<TeacherInfoResponse> getInfo(@AuthenticationPrincipal Teacher teacher) {
        TeacherInfoResponse response = teacherService.getInfo(teacher);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "[강사] 회원정보 수정",
            description = "✅ name, email, password는 값을 null이나 빈칸으로 넣어줄 경우 값이 변경되지 않습니다! 빈칸이 되면 안되기 때문.. " +
                    "<br/> ✅ briefIntroduction, academicBackground, specialization은 필수로 넣어줘야 null로 설정되지 않습니다." +
                    "<br/> ✅ /check/password 를 통해 발급받은 임시 토큰을 넣어줘야 성공합니다. 아니면 403이 나옴"
    )
    @PatchMapping
    public ResponseEntity<TeacherInfoResponse> updateInfo(@RequestBody @Valid TeacherInfoRequest request, @AuthenticationPrincipal Teacher teacher) {
        return ResponseEntity.ok(teacherService.updateInfo(request, teacher));
    }

    @Operation(summary = "[강사] 선생님이 강의하는 커리큘럼 조회", description = "currentPageNumber: 현재 몇 페이지<br/>totalPage: 전체 페이지 개수<br/>pageSize: 한 페이지당 원소 개수(n개씩보기)")
    @GetMapping("/curricula")
    public ResponseEntity<CurriculumListResponse> getMyCourses(@AuthenticationPrincipal Teacher teacher,
                                                               @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                               @RequestParam(value = "currentPageNumber", required = false) Integer currentPageNumber) {
        if (currentPageNumber != null && pageSize != null) {
            int pageNumber = currentPageNumber - 1; //입력은 1부터 시작, 실제로는 0부터 시작
            Pageable pageable = PageRequest.of(pageNumber, pageSize);

            CurriculumListResponse curriculumListResponse = curriculumService.getTeachersCurricula(teacher, pageable);

            return ResponseEntity.ok(curriculumListResponse);
        } else {
            //전체 다 주기 TODO 리팩토링
            CurriculumListResponse curriculumListResponse = curriculumService.getTeachersCurricula(teacher);

            return ResponseEntity.ok(curriculumListResponse);
        }
    }
}
