package hello.board.domain.post;

import hello.board.web.post.PostPagingInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class H2PostRepository implements PostRepository{

    private final EntityManager em;

    @Override
    public Long save(Post post) {
        em.persist(post);
        return post.getId();
    }

    @Override
    public void delete(Long postId) {
        em.remove(postId);
    }

    @Override
    public Post findById(Long id) {
        return em.createQuery("select p from Post p where p.id = :id", Post.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public List<Post> findListWithMember(int offset, int limit) {
        return em.createQuery(
                "select p from Post p"+
                        " join fetch p.member m order by p.id desc", Post.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
}
