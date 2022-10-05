package hello.board.domain.post;

import hello.board.domain.file.UploadFile;
import hello.board.web.post.PostCreateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    Post create(Long memberId, List<UploadFile> attachFiles, PostCreateDto postCreateDto);

    void update(Long postId, String title, String content, List<UploadFile> attachFiles);

    void delete(Long postId);

    Post findOne(Long postId);

    /** 조회 */
    Post viewPost(Long postId);

    Page<Post> searchPagePosts(PostSearchCondition postSearchCondition, Pageable pageable);
}
