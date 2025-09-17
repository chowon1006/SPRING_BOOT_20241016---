package com.example.demo;
 import org.springframework.stereotype.Controller;
 import org.springframework.ui.Model;
 import org.springframework.web.bind.annotation.GetMapping;
 @Controller // 컨트롤러 어노테이션 명시

 public class DemoController {
    @GetMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("greeting", "안녕하세요!");
        model.addAttribute("message1", "방갑습니다.");
        return "hello"; // hello.html 파일을 화면에 보여줍니다.
    }

    // 브라우저에서 /hello2 주소로 접속하면 이 부분이 실행됩니다.
    @GetMapping("/hello2")
    public String hello2(Model model) {
        model.addAttribute("greeting", "안녕하세요!");
        model.addAttribute("username", "이초원님.");
        model.addAttribute("message1", "방갑습니다.");
        model.addAttribute("message2", "오늘.");
        model.addAttribute("message3", "날씨는.");
        model.addAttribute("message4", "매우 좋습니다.");
        return "hello2"; // hello2.html 파일을 화면에 보여줍니다.
    }
     @GetMapping("/about_detailed")
 public String about() {
 return "about_detailed";
 }

}

