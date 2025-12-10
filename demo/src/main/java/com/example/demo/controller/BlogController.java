package com.example.demo.controller;

import com.example.demo.model.domain.Board;
import com.example.demo.model.service.AddArticleRequest;
import com.example.demo.model.service.BlogService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
//import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

//import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class BlogController {

    private final BlogService blogService;

    // // 게시글 목록
    // @GetMapping("/article_list")
    // public String list(Model model) {
    //     model.addAttribute("articles", blogService.findAll());
    //     return "article_list"; // templates/article_list.html
    // }

    // 게시글 수정 폼
    @GetMapping("/article_edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        var opt = blogService.findById(id);
        if (opt.isEmpty()) {
            return "error"; // 없는 글이면 에러 페이지
        }
        model.addAttribute("article", opt.get());
        return "article_edit"; // templates/article_edit.html
    }

    // 게시글 수정 저장
    @PostMapping("/article_update")
    public String update(@RequestParam Long id, AddArticleRequest req) {
        blogService.update(id, req);
        return "redirect:/article_list";
    }

    // 게시글 작성 저장
    @PostMapping("/article_write")
    public String write(AddArticleRequest req) {
        blogService.save(req);
        return "redirect:/article_list";
    }

    // 게시글 삭제
    @PostMapping("/article_delete/{id}")
    public String delete(@PathVariable Long id) {
        blogService.delete(id);
        return "redirect:/article_list";
    }

    // 삭제 (게시판 글 삭제)
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

    // 글쓰기 게시판 저장
    @PostMapping("/api/boards")
    public String addboards(@ModelAttribute AddArticleRequest request) {
        blogService.save(request);
        return "redirect:/board_list"; // .HTML 연결
    }

    // 게시판 목록 (세션 체크 + 페이징 + 검색)
    @GetMapping("/board_list")
    public String board_list(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "") String keyword,
            HttpSession session) { // 세션 객체 전달

        // ✅ 세션에서 userId 확인
        String userId = (String) session.getAttribute("userId");
        String email = (String) session.getAttribute("email"); // 세션에서 이메일 확인

        if (userId == null) {
            return "redirect:/member_login"; // 로그인 페이지로 리다이렉션
        }

        System.out.println("세션 userId: " + userId); // 서버 IDE 터미널에 세션 값 출력

        int pageSize = 3; // 한 페이지당 글 수
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Board> list; // Page를 반환
        if (keyword.isEmpty()) {
            list = blogService.findAll(pageable); // 기본 전체 출력(키워드 x)
        } else {
            list = blogService.searchByKeyword(keyword, pageable); // 키워드로 검색
        }

        int startNum = page * pageSize + 1;
        model.addAttribute("startNum", startNum);
        model.addAttribute("email", email); // 로그인 사용자(이메일)

        model.addAttribute("boards", list.getContent());
        model.addAttribute("totalPages", list.getTotalPages()); // 전체 페이지 수
        model.addAttribute("currentPage", page);                // 현재 페이지 번호
        model.addAttribute("keyword", keyword);                 // 검색 키워드

        return "board_list"; // .HTML 연결
    }

    // 게시판 글 상세보기
    @GetMapping("/board_view/{id}")
    public String boardView(Model model, @PathVariable Long id) {
        Optional<Board> board = blogService.findById(id);
        if (board.isPresent()) {
            model.addAttribute("boards", board.get()); // 단일 객체 바인딩
        } else {
            return "/error_page/article_error"; // 에러 페이지
        }
        return "board_view";
    }

    @GetMapping("/board_edit/{id}") // 게시판 글 수정 페이지 연결
    public String board_edit(@PathVariable Long id, Model model) {
        Board board = blogService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid board Id:" + id));
            model.addAttribute("board", board);
            
            return "board_edit"; // .HTML 연결
}
}