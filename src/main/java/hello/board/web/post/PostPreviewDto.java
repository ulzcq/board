package hello.board.web.post;

import hello.board.domain.member.Member;
import hello.board.domain.post.Post;
import lombok.Data;

import java.time.LocalDateTime;

//entity는 DTO에 의존해서는 안되지만 DTO는 entity를 알아도 괜찮(?)음
@Data
public class PostPreviewDto {

    private Long postId;
    private String writer; //작성자
    private String title; //제목
    private String content; //내용(검색용)
    private LocalDateTime date; //작성 날짜
    private int views;//조회수

    public PostPreviewDto(Post post) {
        this.postId = post.getId();
        this.writer = post.getMember().getName(); //LAZY 초기화
        this.title = post.getTitle();
        this.content = post.getContent();
        this.date = post.getDate();
        this.views = post.getViews();
    }
}
