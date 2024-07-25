package com.twentyone.steachserver.domain.member.memberController;

import com.twentyone.steachserver.domain.curriculum.dto.CurriculumListResponse;
import com.twentyone.steachserver.domain.curriculum.service.CurriculumService;
import com.twentyone.steachserver.domain.member.dto.TeacherInfoRequest;
import com.twentyone.steachserver.domain.member.dto.TeacherInfoResponse;
import com.twentyone.steachserver.domain.member.model.Teacher;
import com.twentyone.steachserver.domain.member.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Operation(summary = "선생님 회원정보 조회")
    @GetMapping
    public ResponseEntity<TeacherInfoResponse> getInfo(@AuthenticationPrincipal Teacher teacher) {
        TeacherInfoResponse response = teacherService.getInfo(teacher);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "선생님 회원정보 수정")
    @PatchMapping
    public ResponseEntity<TeacherInfoResponse> updateInfo(@RequestBody TeacherInfoRequest request, @AuthenticationPrincipal Teacher teacher) {
        return ResponseEntity.ok(teacherService.updateInfo(request, teacher));
    }

    @Operation(summary = "선생님이 강의하는 커리큘럼 조회", description = "currentPageNumber: 현재 몇 페이지, totalPage: 전체 페이지 개수, pageSize: 한 페이지당 원소 개수(n개씩보기)")
    @GetMapping("/curricula")
    public ResponseEntity<CurriculumListResponse> getMyCourses(@AuthenticationPrincipal Teacher teacher,
                                                               @RequestParam(value = "pageSize", required = false, defaultValue = "100") Integer pageSize,
                                                               @RequestParam(value = "currentPageNumber", required = false, defaultValue = "1") Integer currentPageNumber) {
        int pageNumber = currentPageNumber - 1; //입력은 1부터 시작, 실제로는 0부터 시작
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        CurriculumListResponse curriculumListResponse = curriculumService.getTeachersCurricula(teacher, pageable);

        return ResponseEntity.ok(curriculumListResponse);
    }
}
