package com.example.demo.controller;

import java.util.List;
import com.example.demo.model.service.BlogService;
import com.example.demo.model.service.AddArticleRequest;
import com.example.demo.model.domain.Board; 
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@RequiredArgsConstructor
@Controller
public class BlogController {

    private final BlogService blogService;

    // 목록
    @GetMapping("/article_list")
    public String list(Model model) {
        model.addAttribute("articles", blogService.findAll());
        return "article_list";  // templates/article_list.html
    }



     @GetMapping("/board_list") // 새로운 게시판 링크 지정
    public String board_list(Model model) {
        List<Board> list = blogService.findAll(); // 게시판 전체 리스트, 기존 Article에서 Board로 변경됨
         model.addAttribute("boards", list); // 모델에 추가
        return "board_list"; // .HTML 연결
    }

    // ✅ 수정 폼 열기 (이게 없어서 404 났던 것)
    @GetMapping("/article_edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        var opt = blogService.findById(id);
        if (opt.isEmpty()) {
            return "error"; // 없는 글이면 에러 페이지로
        }
        model.addAttribute("article", opt.get());
        return "article_edit";  // templates/article_edit.html 로 렌더
    }

    // 수정 저장 (B안: 액션을 /article_update 로 고정)
    @PostMapping("/article_update")
    public String update(@RequestParam Long id, AddArticleRequest req) {
        blogService.update(id, req);
        return "redirect:/article_list";
    }

    // 글쓰기 저장
    @PostMapping("/article_write")
    public String write(AddArticleRequest req) {
        blogService.save(req);
        return "redirect:/article_list";
    }

    // 삭제
    @PostMapping("/article_delete/{id}")
    public String delete(@PathVariable Long id) {
        blogService.delete(id);
        return "redirect:/article_list";
    }
}
