package com.twentyone.steachserver.domain.member.memberController;

import com.twentyone.steachserver.domain.curriculum.dto.CurriculumListResponse;
import com.twentyone.steachserver.domain.curriculum.service.CurriculumService;
import com.twentyone.steachserver.domain.member.dto.StudentInfoRequest;
import com.twentyone.steachserver.domain.member.dto.StudentInfoResponse;
import com.twentyone.steachserver.domain.member.model.Student;
import com.twentyone.steachserver.domain.member.service.StudentService;
import com.twentyone.steachserver.domain.studentCurriculum.dto.IsApplyForCurriculumResponseDto;
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

@Tag(name = "학생")
@Secured("ROLE_STUDENT")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/students")
public class StudentController {
    private final StudentService studentService;
    private final CurriculumService curriculumService;

    @Operation(summary = "[학생] 회원정보 조회")
    @GetMapping
    public ResponseEntity<StudentInfoResponse> getInfo(@AuthenticationPrincipal Student student) {
        StudentInfoResponse response = studentService.getInfo(student);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "[학생] 회원정보 수정",
            description = "✅ name, email, password는 값을 null이나 빈칸으로 넣어줄 경우 값이 변경되지 않습니다! 빈칸이 되면 안되기 때문.. " +
                    "<br/> ✅ /check/password 를 통해 발급받은 임시 토큰을 넣어줘야 성공합니다. 아니면 403이 나옴"
    )
    @PatchMapping
    public ResponseEntity<StudentInfoResponse> updateInfo(@RequestBody @Valid StudentInfoRequest request, @AuthenticationPrincipal Student student) {
        return ResponseEntity.ok(studentService.updateInfo(request, student));
    }

    @Operation(summary = "[학생] 학생이 수강하는 커리큘럼 조회", description = "currentPageNumber: 현재 몇 페이지, totalPage: 전체 페이지 개수, pageSize: 한 페이지당 원소 개수(n개씩보기)")
    @GetMapping("/curricula")
    public ResponseEntity<CurriculumListResponse> getMyCourses(@AuthenticationPrincipal Student student,
                                                               @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                               @RequestParam(value = "currentPageNumber", required = false) Integer currentPageNumber) {
        if (pageSize != null && currentPageNumber != null) {
            //페이징 있는 버전
            int pageNumber = currentPageNumber - 1; //입력은 1부터 시작, 실제로는 0부터 시작
            Pageable pageable = PageRequest.of(pageNumber, pageSize);

            CurriculumListResponse curriculumListResponse = curriculumService.getStudentsCurricula(student, pageable);
            return ResponseEntity.ok(curriculumListResponse);
        }

        //페이징 없는 버전
        CurriculumListResponse curriculumListResponse = curriculumService.getStudentsCurricula(student);
        return ResponseEntity.ok(curriculumListResponse);
    }

    @Operation(summary = "[학생] 학생이 커리큘럼을 수강 신청 여부", description = "이미 신청했으면 true")
    @GetMapping("/check/curriculum-apply/{curriculumId}")
    public ResponseEntity<IsApplyForCurriculumResponseDto> getIsApplyForCurriculum(@AuthenticationPrincipal Student student,
                                                                                   @PathVariable Integer curriculumId) {
        Boolean isApplyForCurriculum = curriculumService.getIsApplyForCurriculum(student, curriculumId);
        return ResponseEntity.ok(IsApplyForCurriculumResponseDto.of(isApplyForCurriculum));
    }
}
