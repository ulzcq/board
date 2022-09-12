package hello.board.domain.post;

import hello.board.web.post.MyPageble;

import java.util.List;

public interface PostRepository {

    Long save(Post post); //글 저장

    void delete(Post post); //글 삭제

    Post findById(Long id);//글 1개 조회

    List<Post> findListWithMember(MyPageble myPageble);//글 5개 조회

    long findAllCnt();

}
