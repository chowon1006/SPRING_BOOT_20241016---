package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value; // 주석 해제
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class FileController {

    // application.properties에 설정된 경로를 가져오기 위해 주석 해제
    @Value("${spring.servlet.multipart.location}")
    private String uploadFolder;

    @PostMapping("/upload-email")
    public String uploadEmail(
            @RequestParam("email") String email,
            @RequestParam("subject") String subject,
            @RequestParam("message") String message,
            RedirectAttributes redirectAttributes) {

        try {
            // 업로드 경로 확인 및 생성
            Path uploadPath = Paths.get(uploadFolder).toAbsolutePath();
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 기본 파일명 생성 (특수문자 제거)
            String sanitizedEmail = email.replaceAll("[^a-zA-Z0-9]", "_");
            String fileName = sanitizedEmail + ".txt";
            Path filePath = uploadPath.resolve(fileName);

            //  중복 파일명 처리 (12주차 연습문제)
            // 파일이 이미 존재하면 뒤에 _1, _2, _3... 숫자를 붙여서 이름 변경
            int count = 1;
            while (Files.exists(filePath)) {
                String newFileName = sanitizedEmail + "_" + count + ".txt";
                filePath = uploadPath.resolve(newFileName);
                count++;
            }

            System.out.println("File path: " + filePath); // 최종 저장 경로 확인

            // 파일 저장 처리
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
                writer.write("메일제목: " + subject);
                writer.newLine();
                writer.write("요청메시지:");
                writer.newLine();
                writer.write(message);
            }

            redirectAttributes.addFlashAttribute("message", "메일 내용이 성공적으로 업로드되었습니다!");

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "업로드 중 오류가 발생했습니다.");
            return "/error_page/article_error";
        }

        return "upload_end";
    }
}