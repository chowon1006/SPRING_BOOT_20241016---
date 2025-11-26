package com.example.demo.model.service;

import com.example.demo.model.domain.Member;
import com.example.demo.model.repository.MemberRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 이메일 중복 체크
     */
    private void validateDuplicateMember(AddMemberRequest request) {

        // MemberRepository에서 findByEmail을 Optional로 반환한다면 아래처럼 변경해야 함.
        Member findMember = memberRepository.findByEmail(request.getEmail());

        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    /**
     * 회원가입 저장
     */
    public Member saveMember(@Valid AddMemberRequest request) {

        // 중복 이메일 검증
        validateDuplicateMember(request);

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        request.setPassword(encodedPassword);

        // DB 저장
        return memberRepository.save(request.toEntity());
    }

    /**
     * 로그인 체크
     */
    public Member loginCheck(String email, String rawPassword) {

        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            throw new IllegalArgumentException("등록되지 않은 이메일입니다.");
        }

        // 비밀번호 일치 확인
        if (!passwordEncoder.matches(rawPassword, member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return member;
    }
}