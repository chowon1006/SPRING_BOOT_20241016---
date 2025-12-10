package com.example.demo.model.service;

import lombok.*; 
import jakarta.validation.constraints.*; // 검증 어노테이션
import com.example.demo.model.domain.Member;

@Data
@NoArgsConstructor 
@AllArgsConstructor 
public class AddMemberRequest {

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @Pattern(regexp = "^[A-Za-z가-힣]+$", message = "이름은 한글 또는 영문만 입력 가능합니다.")
    private String name;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z]).{8,20}$",
        message = "비밀번호는 8자 이상 20자 이하이며, 대문자와 소문자를 모두 포함해야 합니다."
    )
    private String password;

    // 나이를 String -> Integer로 변경하여 @Min, @Max 사용
    @NotNull(message = "나이는 필수 입력 값입니다.")
    @Min(value = 19, message = "19세 이상만 가입 가능합니다.")
    @Max(value = 90, message = "90세 이하만 가입 가능합니다.")
    private Integer age;

    // 휴대폰 번호는 선택 항목이므로 빈 값 허용 (패턴은 유지)
    @Pattern(
        regexp = "^$|^01[0-9]-?\\d{3,4}-?\\d{4}$",
        message = "휴대폰 번호 형식이 올바르지 않습니다."
    )
    private String mobile;

    @NotBlank(message = "주소는 필수 입력 값입니다.")
    private String address;

    public Member toEntity() { 
        return Member.builder()
            .name(name)
            .email(email)
            .password(password)
            // Integer로 받은 나이를 DB 저장을 위해 String으로 변환
            .age(String.valueOf(age)) 
            .mobile(mobile)
            .address(address)
            .build();
    }
}