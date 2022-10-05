package hello.board.domain.post;

import lombok.Data;

@Data
public class PostSearchCondition {

    private String writer; //작성자로 검색
    private String title; //제목으로 검색
    private String content; //내용으로 검색
}
