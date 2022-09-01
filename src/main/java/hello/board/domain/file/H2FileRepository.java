package hello.board.domain.file;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class H2FileRepository implements FileRepository{

    private final EntityManager em;

    @Override
    public UploadFile findById(Long fileId) {
        return em.find(UploadFile.class, fileId);
    }

    @Override
    public List<UploadFile> findAllByPostId(Long postId) {
        return em.createQuery("select p.attachFiles from Post p where p.id =: postId", UploadFile.class)
                .setParameter("postId", postId)
                .getResultList();
    }

    @Override
    public void delete(Long fileId) {
        UploadFile findFile = findById(fileId);
        em.remove(findFile);
    }

    @Override
    public void deleteAllByPostId(Long postId) {
        em.createQuery("delete from UploadFile where post.id =: postId", UploadFile.class)
                .setParameter("postId", postId);
    }
}
