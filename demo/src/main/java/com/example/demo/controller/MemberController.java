package com.example.demo.controller;

import com.example.demo.model.domain.Member;
import com.example.demo.model.service.AddMemberRequest;
import com.example.demo.model.service.MemberService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
        // 빈 객체를 넘겨줘야 Thymeleaf가 th:object로 잡아서 에러를 표시할 수 있음
        model.addAttribute("request", new AddMemberRequest());
        return "join_new"; 
    }

    // 회원 가입 저장
    @PostMapping("/api/members")
    public String addmembers(@Valid @ModelAttribute("request") AddMemberRequest request,
                             BindingResult bindingResult,
                             Model model) {

        // 검증 실패 시 다시 가입 페이지로 (에러 메시지 포함됨)
        if (bindingResult.hasErrors()) {
            return "join_new";
        }

        memberService.saveMember(request);
        return "join_end"; 
    }

    // 로그인 페이지
    @GetMapping("/member_login")
    public String member_login() {
        return "login"; 
    }

    // 로그인 체크
    @PostMapping("/api/login_check")
    public String checkMembers(@ModelAttribute AddMemberRequest request,
                               Model model,
                               HttpServletRequest httpRequest,
                               HttpServletResponse response) {
        try {
            // 1. 보안을 위해 기존 세션/쿠키 제거
            HttpSession session = httpRequest.getSession(false); 
            if (session != null) {
                session.invalidate(); 
            }
            Cookie cookie = new Cookie("JSESSIONID", null);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);

            // 2. 새 세션 생성
            session = httpRequest.getSession(true); 

            // 3. 로그인 검증
            Member member = memberService.loginCheck(request.getEmail(), request.getPassword());

            // 4. 세션에 값 저장
            String sessionId = UUID.randomUUID().toString();
            session.setAttribute("userId", sessionId);
            session.setAttribute("email", request.getEmail());

            session.setAttribute("userName", member.getName());

            return "redirect:/board_list"; 
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage()); 
            return "login"; 
        }
    }

    // [추가된 부분] 로그아웃 기능
    @GetMapping("/api/logout")
    public String member_logout(HttpServletRequest request, HttpServletResponse response) {
        // 세션 무효화
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        // 쿠키 삭제
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/member_login"; // 로그아웃 후 로그인 페이지로 이동
    }
}