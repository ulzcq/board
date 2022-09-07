package hello.board.web.post;

import hello.board.domain.post.Post;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 *  게시글 등록 & 수정용 DTO
 */
@Data
public class CreatePostDto {

    @NotBlank
    private String title; //제목

    @NotBlank
    private String content; //내용

    private Long memberId; //작성자 id
    private List<MultipartFile> multipartFiles = new ArrayList<>(); //새로 첨부한 파일

    public Post toEntity(){
        return new Post(title, content, LocalDateTime.now(), 0);
    }

}
