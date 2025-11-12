package com.example.demo.model.service;
//import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.demo.model.domain.Board;
import com.example.demo.model.repository.BoardRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // final 필드 자동 생성자 주입
public class BlogService {

    @Autowired // 생성자 1개면 사실 생략 가능
    private final BoardRepository boardRepository2; // 게시판용 리포지토리

    // 전체 목록 조회
    // public List<Board> findAll() {
    //     return boardRepository2.findAll();
    // }

    // 게시글 저장
    public Board save(AddArticleRequest request) {
        return boardRepository2.save(request.toEntity());
    }

    // 특정 게시글 조회
    public Optional<Board> findById(Long id) {
        return boardRepository2.findById(id);
    }

    // 게시글 수정
    public void update(Long id, AddArticleRequest request) {
        Optional<Board> optionalBoard = boardRepository2.findById(id);
        optionalBoard.ifPresent(board -> {
            board.update(request.getTitle(), request.getContent());
            boardRepository2.save(board);
        });
    }

    // 게시글 삭제
    public void delete(Long id) {
        boardRepository2.deleteById(id);
    }

    public Page<Board> findAll(Pageable pageable) {
    return boardRepository2.findAll(pageable);
    }
    public Page<Board> searchByKeyword(String keyword, Pageable pageable) {
    return boardRepository2.findByTitleContainingIgnoreCase(keyword, pageable);
    } // LIKE 검색 제공(대소문자 무시)
}