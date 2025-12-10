package com.example.demo.controller;

import com.example.demo.model.domain.Board;
import com.example.demo.model.service.AddArticleRequest;
import com.example.demo.model.service.BlogService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class BlogController {

    private final BlogService blogService;

    
    // 9주차 연습문제 게시글 삭제 기능
    @PostMapping("/api/board_delete/{id}")
    public String deleteBoard(@PathVariable Long id) {
        blogService.delete(id);
        return "redirect:/board_list";    // 삭제 후 목록으로 이동
    }

    // 게시판 글쓰기 페이지
    @GetMapping("/board_write")
    public String boardWrite() {
        return "board_write";
    }

   // 11주차 연습문제 글쓰기 게시판 저장 (작성자 추가)
    @PostMapping("/api/boards")
    public String addboards(@ModelAttribute AddArticleRequest request, HttpSession session) {
        // 1. 세션에서 로그인한 사용자 이메일(ID) 가져오기
        String email = (String) session.getAttribute("email");

        // 2. 로그인이 안 되어 있다면 로그인 페이지로 리다이렉트 (안전 장치)
        if (email == null) {
            return "redirect:/member_login";
        }

        // 3. DTO에 작성자(user)를 현재 로그인한 이메일로 설정
        request.setUser(email);

        blogService.save(request);
        return "redirect:/board_list";
    }

   
    // 9주차 연습문제 게시판 목록
    @GetMapping("/board_list")
    public String board_list(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "") String keyword,
            HttpSession session) { // 세션 객체 전달

        // 세션에서 userId 확인 (로그인 체크)
        String userId = (String) session.getAttribute("userId");
        String email = (String) session.getAttribute("email");

        if (userId == null) {
            return "redirect:/member_login"; // 로그인 안 했으면 로그인 페이지로
        }

        System.out.println("세션 userId: " + userId);

        int pageSize = 3; // 한 페이지당 글 수
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Board> list;
        if (keyword.isEmpty()) {
            list = blogService.findAll(pageable); // 전체 조회
        } else {
            list = blogService.searchByKeyword(keyword, pageable); // 검색 조회
        }

        // 화면용 글 번호 계산 
        // 페이지가 넘어가도 번호가 이어지도록 계산
        int startNum = page * pageSize + 1;
        
        model.addAttribute("startNum", startNum);
        model.addAttribute("email", email); 

        model.addAttribute("boards", list.getContent()); // 리스트 데이터
        model.addAttribute("totalPages", list.getTotalPages()); // 전체 페이지 수
        model.addAttribute("currentPage", page);                // 현재 페이지 번호
        model.addAttribute("keyword", keyword);                 // 검색 키워드 유지

        return "board_list"; 
    }

    // 11주차 연습문제 게시판 글 상세보기 (로그인 정보 전달)
    @GetMapping("/board_view/{id}")
    public String boardView(Model model, @PathVariable Long id, HttpSession session) {
        Optional<Board> board = blogService.findById(id);
        
        if (board.isPresent()) {
            model.addAttribute("board", board.get());
            
            // 현재 로그인한 사용자 아이디(이메일)를 모델에 추가
            String loginUser = (String) session.getAttribute("email");
            model.addAttribute("loginUser", loginUser); 
        } else {
            return "/error_page/article_error"; 
        }
        return "board_view";
    }

    // 게시판 글 수정 페이지 연결
    @GetMapping("/board_edit/{id}")
    public String board_edit(@PathVariable Long id, Model model) {
        Board board = blogService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid board Id:" + id));
        model.addAttribute("board", board);
        return "board_edit";
    }

    // 수정 완료 버튼을 눌렀을 때 실제로 데이터를 수정하는 부분
    @PutMapping("/api/boards/{id}")
    public String updateBoard(@PathVariable Long id, @ModelAttribute AddArticleRequest request) {
        blogService.update(id, request);
        return "redirect:/board_list";
    }
}