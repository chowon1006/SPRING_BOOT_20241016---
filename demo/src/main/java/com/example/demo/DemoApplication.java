package com.example.demo; // 현재폴더위치
import org.springframework.boot.SpringApplication; // 스프링핵심클래스
import org.springframework.boot.autoconfigure.SpringBootApplication; // 자동설정기능활성화

@SpringBootApplication // 애노테이션(스프링부트APP 명시, 하위다양한설정을자동등록)
 public class DemoApplication { // 클래스이름
public static void main(String[] args) { // 메인메서드(프로그램시작점)
 SpringApplication.run(DemoApplication.class, args); // run 메서드로실행
}
 }