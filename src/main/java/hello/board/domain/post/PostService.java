package hello.board.domain.post;

import hello.board.domain.file.UploadFile;

import java.util.List;

public interface PostService {

    Long create(Long memberId, List<UploadFile> attachFiles, Post post);

    void update(Long postId, String title, String content, List<UploadFile> attachFiles);

    void delete(Long postId);

    Post findOne(Long postId);

    /** 조회 */
    Post viewPost(Long postId);

    List<Post> getPagePosts(int offset, int limit);
}
