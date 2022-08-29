package hello.board.domain.file;

import hello.board.domain.post.Post;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class UploadFile {

    @Id @GeneratedValue
    @Column(name = "file_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String uploadFileName; //고객이 업로드한 파일명
    private String storedFileName; //서버 내부에서 관리하는 파일명

    private Long fileSize;

    public UploadFile(String uploadFileName, String storedFileName, Long fileSize) {
        this.uploadFileName = uploadFileName;
        this.storedFileName = storedFileName;
        this.fileSize = fileSize;
    }

    /** 연관관계 편의 메서드 */
    public void setPost(Post post){
        this.post = post;
        post.getAttachFiles().add(this);
    }
}
