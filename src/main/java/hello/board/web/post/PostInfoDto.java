package hello.board.web.post;

import hello.board.domain.post.Post;

import hello.board.web.file.UploadFileDto;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
public class PostInfoDto {

    private String writer; //작성자
    private Long writerId; //작성자 id
    private String title; //제목
    private String content; //내용
    private LocalDateTime date; //작성 날짜
    private int views;//조회수

    //private List<UploadFile> uploadFiles; //엔티티에 의존하면 안된다. 이것도 DTO로 변환
    private List<UploadFileDto> uploadFiles;

    public PostInfoDto(Post post) {
        this.writer = post.getMember().getName();
        this.writerId = post.getMember().getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.date = post.getDate();
        this.views = post.getViews();

        //DTO로 변환
        this.uploadFiles = post.getAttachFiles().stream()
                .map(uploadFile -> new UploadFileDto(uploadFile))
                .collect(toList());
    }
}
