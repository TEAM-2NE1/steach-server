package com.twentyone.steachserver.domain.auth.controller;

import com.twentyone.steachserver.domain.auth.dto.*;
import com.twentyone.steachserver.domain.auth.model.LoginCredential;
import com.twentyone.steachserver.domain.auth.service.AuthService;
import com.twentyone.steachserver.domain.member.service.StudentService;
import com.twentyone.steachserver.domain.member.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Tag(name = "인증")
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;
    private final StudentService studentService;
    private final TeacherService teacherService;

    @Operation(summary = "[All] 학생 회원가입!", description = "username은 빈칸 불가능, 숫자로 시작 불가능, 영어와 숫자 조합만 가능")
    @PostMapping("/student/join")
    public ResponseEntity<LoginResponseDto> signupStudent(@RequestBody StudentSignUpDto studentSignUpDto) {
        LoginResponseDto loginResponseDto = authService.signUpStudent(studentSignUpDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(loginResponseDto);
    }

    @Operation(summary = "[student] 학생 닉네임 중복확인")
    @GetMapping("/student/check-nickname/{nickname}")
    public ResponseEntity<CheckUsernameAvailableResponse> checkNicknameAvailability(@PathVariable("nickname") String nickname) {
        return ResponseEntity.ok(studentService.checkNicknameAvailability(nickname));
    }

    @Operation(summary = "[All] 강사 회원가입!", description = "username은 빈칸 불가능, 숫자로 시작 불가능, 영어와 숫자 조합만 가능/ file은 첨부하지 않아도 됨(추후 변경예정) <br/> auth_code 사용안한 것만 가능")
    @PostMapping(value = "/teacher/join", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<LoginResponseDto> signupTeacher(@RequestPart(value = "file", required = false) MultipartFile file, @RequestPart("teacherSignUpDto") TeacherSignUpDto teacherSignUpDto) throws IOException {
        LoginResponseDto loginResponseDto = authService.signUpTeacher(teacherSignUpDto, file);

        return ResponseEntity.status(HttpStatus.CREATED).body(loginResponseDto);
    }

    @Operation(summary = "[All] 로그인!")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(authService.login(loginDto));
    }

    @Operation(summary = "[All] 아이디 중복확인")
    @GetMapping("/check-username/{username}")
    public ResponseEntity<CheckUsernameAvailableResponse> checkUsernameAvailability(@PathVariable("username") String username) {
        return ResponseEntity.ok(authService.checkUsernameAvailability(username));
    }

    @Operation(summary = "[All] 선생님 이메일 중복확인")
    @GetMapping("/teacher/check-email/{email}")
    public ResponseEntity<CheckUsernameAvailableResponse> checkTeacherEmailAvailability(@PathVariable("email") String email) {
        return ResponseEntity.ok(teacherService.checkEmailAvailability(email));
    }

    @Operation(summary = "[All] 학생 이메일 중복확인")
    @GetMapping("/student/check-email/{email}")
    public ResponseEntity<CheckUsernameAvailableResponse> checkStudentEmailAvailability(@PathVariable("email") String email) {
        return ResponseEntity.ok(studentService.checkEmailAvailability(email));
    }

    @Operation(summary = "[인증된 사용자] 비밀번호 체크", description = "60분짜리 임시토큰이 발급됩니다. 회원정보 수정 시 사용합니다.")
    @PostMapping("/check/password")
    public ResponseEntity<MemberCheckPasswordResponseDto> checkPassword(@AuthenticationPrincipal LoginCredential loginCredential, @RequestBody MemberCheckPasswordRequestDto checkPasswordRequestDto) {
        return ResponseEntity.ok(authService.checkPassword(loginCredential, checkPasswordRequestDto));
    }

    @Operation(summary = "[인증된 사용자] 회원 삭제", description = "강사일 때, 내가 만든 커리큘럼은 삭제되지 않음")
    @DeleteMapping("/member")
    public ResponseEntity<Void> deleteMember(@AuthenticationPrincipal LoginCredential loginCredential) {
        authService.deleteMember(loginCredential);

        return ResponseEntity.ok().build();
    }
}
