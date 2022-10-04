package hello.board.web.file;

import hello.board.PostConst;
import hello.board.domain.file.UploadFile;
import lombok.Data;

@Data
public class UploadFileDto {

    private Long fileId;
    private String uploadFileName; //고객이 업로드한 파일명
    private String storedFileName; //서버 내부에서 관리하는 파일명
    private Long fileSize;

    //LAZY 초기화
    public UploadFileDto(UploadFile uploadFile) {
        this.fileId = uploadFile.getId();
        this.uploadFileName = uploadFile.getUploadFileName();
        this.storedFileName = uploadFile.getStoredFileName();
        this.fileSize = uploadFile.getFileSize()/ PostConst.KB_TO_BYTE;
    }
}
