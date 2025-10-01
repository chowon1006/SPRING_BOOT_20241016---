package com.example.demo.controller;
import  com.example.demo.model.service.TestService; // 최상단 서비스 클래스 연동 추가
import com.example.demo.model.domain.TestDB; // TestDB 클래스 import 추가

 import org.springframework.beans.factory.annotation.Autowired;
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

 @GetMapping("/test1")
 public String thymeleaf_test1(Model model) {
 model.addAttribute("data1", "<h2> 방갑습니다 </h2>");
 model.addAttribute("data2", "태그의 속성 값");
 model.addAttribute("link", 01);
 model.addAttribute("name", "이초원");
 model.addAttribute("para1", "001");
 model.addAttribute("para2", 002);
 return "thymeleaf_test1";
 }

 // 클래스 하단 작성
@Autowired
TestService testService; // DemoController 클래스 아래 객체 생성
// 하단에 맵핑 이어서 추가
@GetMapping("/testdb")
 public String getAllTestDBs(Model model) {
 TestDB test = testService.findByName("이초원");
 model.addAttribute("data4", test);
 System.out.println("데이터 출력 디버그 : " + test);
 return "testdb";
    }
 }
//  @GetMapping("/article_list")
//  public String article_list() {
//  return "article_list";
//  }
// }

