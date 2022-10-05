package hello.board.domain.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPostRepository {
    Page<Post> searchPage(PostSearchCondition condition, Pageable pageable);
}
