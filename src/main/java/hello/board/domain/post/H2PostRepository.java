package hello.board.domain.post;

import hello.board.web.post.MyPageble;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
    public void delete(Post post) {
        em.remove(post);
    }

    @Override
    public Post findById(Long id) {
        try {
            return em.createQuery("select p from Post p where p.id = :id", Post.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch(NoResultException e){
            throw new NoResultException(); //TODO
        }
    }

    public List<Post> findListWithMember(MyPageble myPageble) {
        return em.createQuery(
                "select p from Post p"+
                        " join fetch p.member m order by p.id desc", Post.class)
                .setFirstResult(myPageble.getPageStat())
                .setMaxResults(myPageble.getSize())
                .getResultList();
    }

    @Override
    public long findAllCnt() {
        return (long) em.createQuery("select count(p) from Post p")
                .getSingleResult();
    }
}
