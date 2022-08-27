package hello.board.domain.post;

import hello.board.domain.file.UploadFile;
import hello.board.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * CascadeType.ALL + orphanRemovel=true
 * - 두 옵션을 모두 활성화 하면 부모 엔티티를 통해서 자식의 생명주기를 관리, 따라서 DAO나 repository가 필요없다
 * - 도메인 주도 설계(DDD)의 Aggregate Root개념을 구현할 때 유용
 * - Aggregate Root: repository는 얘만 contect하고 나머지는 만들지 않는 것이 더 유용하다. 얘를 통해서 생명주기를 관리한다.
 */
@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; //작성자

    @Column(length = 40, nullable = false)
    private String title; //제목

    @Column(nullable = false)
    private String content; //내용

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<UploadFile> attachFiles = new ArrayList<>(); //첨부파일 여러개

    @Column(nullable = false)
    private LocalDateTime date; //작성 날짜

    @Column(nullable = false)
    private int views;//조회수

    /**
     * 자식*---1부모, 여기선 내가 자식(*연관관계의 주인)이니까 내가 갖는 parent값은 외래키
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Post parent;//부모 게시판(내가 답글단)

////    @OneToMany(mappedBy = "parent")
////    private List<Post> child = new ArrayList<>(); //자식 게시판(여기에 답글달린)

    /** update 메서드 */
    public void change(String title, String content, List<UploadFile> attachFiles) {
        this.title = title;
        this.content = content;
        this.attachFiles = attachFiles;
    }

    /** 조회수 증가 */
    public int cntViews() {
        return ++this.views;
    }

    public Post(String title, String content, LocalDateTime date, int views) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.views = views;
    }

    /** 연관관계 편의 메서드 : 핵심적으로 컨트롤하는 쪽이 들고있는게 좋다. */
    public void setMember(Member member) {
        this.member = member;
        member.getPosts().add(this);
    }
}