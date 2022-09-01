package hello.board.domain.file;

import java.util.List;

public interface FileRepository {
    UploadFile findById(Long fileId);

    List<UploadFile> findAllByPostId(Long postId);

    void delete(Long fileId);

    void deleteAllByPostId(Long postId);
}
