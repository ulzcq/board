package hello.board.web.post;

import hello.board.domain.post.Post;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

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

    /** 수정용 **/
    private List<UploadFileDto> storedFiles; //원래 포스팅에 저장된 파일(DB)
    private List<Long> nonDeletedFileId = new ArrayList<>(); //삭제된 파일 id 리스트

    public Post toEntity(){
        return new Post(title, content, LocalDateTime.now(), 0);
    }

    public CreatePostDto(){} //없으면 에러남(?)

    /** 수정용 **/
    public CreatePostDto(Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
        this.memberId = post.getMember().getId();

        //DTO로 변환
        this.storedFiles = post.getAttachFiles().stream()
                .map(uploadFile -> new UploadFileDto(uploadFile))
                .collect(toList());
    }
}
