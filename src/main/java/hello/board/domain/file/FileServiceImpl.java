package hello.board.domain.file;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** MultipartFile을 서버에 저장하는 역할 담당 (서비스) */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileServiceImpl {

    private final H2FileRepository fileRepository;

    @Value("${custom.path.file-dir}")
    private String fileDir;

    public String getFullPath(String filename){
        return fileDir + filename;
    }

    /** 파일 한 개 업로드
     * @return null : 실패
     * @return UploadFile : 성공
     */
    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if(multipartFile.isEmpty()){
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);

        //서버 저장용 경로로 파일 저장
        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        return new UploadFile(originalFilename, storeFileName, multipartFile.getSize());
    }

    /** 파일 여러 개 업로드 */
    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if(!multipartFile.isEmpty()){
                storeFileResult.add(storeFile(multipartFile));
            }
        }
        return storeFileResult;
    }

    /** 서버 저장용 파일이름 생성 */
    private String createStoreFileName(String originalFilename){
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext; // "서버 내부관리 파일명.확장자"
    }

    /** 확장자 추출(필수는 아닌데 이렇게 해놓으면 운영하기가 편하다) 예) png */
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    /** 파일 조회 */
    public List<UploadFile> findFileList(Long postId){
        return fileRepository.findAllByPostId(postId);
    }

    /** 파일 삭제 메서드 */
    public void deleteFile(Long fileId){
        fileRepository.delete(fileId);
    }

    /** 해당 게시글의 모든 파일 삭제*/
    public void deleteAllFile(Long postId){
        fileRepository.deleteAllByPostId(postId);
    }
}
