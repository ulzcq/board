package hello.board.domain.post;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository{

    //fetch 조인의 간편 버전, left outer join
    @EntityGraph(attributePaths = "member")
    Post findWithMemberById(@Param("id") Long id);
}
