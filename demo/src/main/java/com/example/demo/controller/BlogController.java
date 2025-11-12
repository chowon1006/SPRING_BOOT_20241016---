package com.example.demo.controller;

import com.example.demo.model.domain.Board;
import com.example.demo.model.service.AddArticleRequest;
import com.example.demo.model.service.BlogService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
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

    

     // 게시글 목록
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


    // //게시판 목록
    // @GetMapping("/board_list")
    // public String boardList(Model model) {
    //     List<Board> list = blogService.findAll();
    //     model.addAttribute("boards", list);
    //     return "board_list";
    // }

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


    @GetMapping("/board_list") // 새로운 게시판 링크 지정
    public String board_list(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "") String keyword) {
    PageRequest pageable = PageRequest.of(page, 3); // 한 페이지의 게시글 수
    Page<Board> list; // Page를 반환
    if (keyword.isEmpty()) {
        list = blogService.findAll(pageable); // 기본 전체 출력(키워드 x)
        } else {
        list = blogService.searchByKeyword(keyword, pageable); // 키워드로 검색
        }
        model.addAttribute("boards", list); // 모델에 추가
        model.addAttribute("totalPages", list.getTotalPages()); // 페이지 크기
        model.addAttribute("currentPage", page); // 페이지 번호
        model.addAttribute("keyword", keyword); // 키워드
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

}
