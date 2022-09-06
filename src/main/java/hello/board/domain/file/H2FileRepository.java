package hello.board.domain.file;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Slf4j
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
        return em.createQuery("select f from UploadFile f where f.post.id = :postId", UploadFile.class)
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
        int deletedCount = em.createQuery("delete from UploadFile f where f.post.id = :postId")
                .setParameter("postId", postId)
                .executeUpdate();

        log.info("삭제된 쿼리 실행 수 : {}", deletedCount);
    }
}
