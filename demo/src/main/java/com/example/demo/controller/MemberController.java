package com.example.demo.controller;

import com.example.demo.model.domain.Member;
import com.example.demo.model.service.AddMemberRequest;
import com.example.demo.model.service.MemberService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;

    // 회원 가입 페이지
    @GetMapping("/join_new")
    public String join_new(Model model) {
        model.addAttribute("request", new AddMemberRequest());
        return "join_new"; // templates/join_new.html
    }

    // 회원 가입 저장
    @PostMapping("/api/members")
    public String addmembers(@ModelAttribute("request") @Valid AddMemberRequest request,
                             BindingResult bindingResult,
                             Model model) {

        // 검증 실패 시 다시 가입 페이지로
        if (bindingResult.hasErrors()) {
            return "join_new";
        }

        memberService.saveMember(request);
        return "join_end"; // 가입 완료 페이지 (또는 redirect:/member_login)
    }

    // 로그인 페이지
    @GetMapping("/member_login")
    public String member_login() {
        return "login"; // templates/login.html
    }

    // 로그인 체크
    @PostMapping("/api/login_check")
    public String checkMembers(@ModelAttribute AddMemberRequest request,
                               Model model,
                               HttpServletRequest httpRequest,
                               HttpServletResponse response) {
        try {
            // 1. 기존 세션/쿠키 제거
            HttpSession session = httpRequest.getSession(false); // 기존 세션(없으면 null)
            if (session != null) {
                session.invalidate(); // 기존 세션 무효화
            }

            // 기존 JSESSIONID 쿠키 삭제
            Cookie cookie = new Cookie("JSESSIONID", null);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);

            // 2. 새 세션 생성
            session = httpRequest.getSession(true); // 새로운 세션

            // 3. 로그인 검증
            Member member = memberService.loginCheck(request.getEmail(), request.getPassword());

            // 4. 세션에 값 저장
            String sessionId = UUID.randomUUID().toString();
            String email = request.getEmail();

            session.setAttribute("userId", sessionId);
            session.setAttribute("email", email);

            // (필요하면 model에 member도 넣을 수 있지만 redirect면 어차피 안 쓰임)
            model.addAttribute("member", member);

            return "redirect:/board_list";        // 로그인 성공 후 이동
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage()); // 에러 메시지 전달
            return "login"; // 로그인 실패 시 다시 로그인 페이지
        }
    }
}