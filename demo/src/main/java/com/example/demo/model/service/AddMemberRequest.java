package com.example.demo.model.service;

import lombok.*; // 어노테이션 자동 생성
import jakarta.validation.constraints.*;
import com.example.demo.model.domain.Member;

@Data
@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자 추가
public class AddMemberRequest {

    @NotBlank(message = "이름은 필수입니다.")
    @Pattern(regexp = "^[A-Za-z가-힣]+$", message = "이름은 한글/영문만 입력하세요.")
    private String name;

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z]).{8,}$",
        message = "비밀번호는 8자 이상이며 대문자와 소문자를 모두 포함해야 합니다."
    )
    private String password;

    @NotBlank(message = "나이는 필수입니다.")
    @Pattern(
        regexp = "^(1[9]|[2-8][0-9]|90)$",
        message = "나이는 19 이상 90 이하만 가능합니다."
    )
    private String age;

    @Pattern(
        regexp = "^$|^01[0-9]-?\\d{3,4}-?\\d{4}$",
        message = "휴대폰 번호 형식이 올바르지 않습니다."
    )
    private String mobile;

    private String address;

    public Member toEntity() { // Member 생성자를 통해 객체 생성
        return Member.builder()
            .name(name)
            .email(email)
            .password(password)
            .age(age)
            .mobile(mobile)
            .address(address)
            .build();
    }
}