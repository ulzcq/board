package hello.board.web.post;

import hello.board.PostConst;
import hello.board.SessionConst;
import hello.board.domain.file.FileServiceImpl;
import hello.board.domain.file.UploadFile;
import hello.board.domain.member.Member;
import hello.board.domain.post.Post;
import hello.board.domain.post.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostServiceImpl postServiceImpl;
    private final FileServiceImpl fileServiceImpl;

    /** 일반 게시글 작성 폼 */
    @GetMapping("/post/new")
    public String createPostForm(@ModelAttribute("post") CreatePostDto createPostDto){
        return "post/createPostForm";
    }

    /** 일반 게시글 작성 */
    @PostMapping("/post/new")
    public String createPost(
            @Validated @ModelAttribute("post") CreatePostDto createPostDto, BindingResult result,
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
            RedirectAttributes redirectAttributes) throws IOException {

        //검증 실패 시
        if(result.hasErrors()){
            return "post/createPostForm";
        }

        //파일 체크
        List<UploadFile> addFileList = checkFiles(createPostDto.getMultipartFiles());
        if (addFileList == null){ //실패 시 다시 폼으로
            result.reject("fileUploadFail", PostConst.FILE_SIZE_EXCEEDED);
            return "post/createPostForm";
        }

        //검증 성공 시 작성 처리 : form -> entity
        Post post = createPostDto.toEntity();

        //게시글 생성
        Long createdId = postServiceImpl.create(loginMember.getId(), addFileList, post);
        if(createdId == null){
            return "post/createPostForm"; //실패 시 다시 폼으로
        }

        //확인 창 띄우기 위한 메시지
        redirectAttributes.addAttribute("create_ok", true);

        return "redirect:/posts";
    }

    /** 파일 용량 체크 및 경로 셋팅 메서드
     * @return null : 용량 초과(10MB)
     */
    private List<UploadFile> checkFiles(List<MultipartFile> toCheckFiles) throws IOException {
        List<UploadFile> addFileList = new ArrayList<>(); //최종 저장될 파일

        for (MultipartFile file : toCheckFiles) {
            if(!file.isEmpty() && file.getSize() > (10 * PostConst.MB_TO_BYTE)) {
                return null;
            } else {
                addFileList.add(fileServiceImpl.storeFile(file)); //파일 이름 셋팅해서 ArrayList에 추가
            }
        }
        return addFileList;
    }


    /** 게시글 리스트 조회
     * - 페이징 필요
     * - 계층형 게시판 구현
     * - 엔티티를 조회해서 DTO로 변환 : JPA 2편 복습(V3.1)
     * - 컬렉션 관계는 hibernate.default_batch_fetch_size, @BatchSize로 최적화
     * - Spring Data JPA 사용 시 Pageble, Page 사용 @PageableDefault(direction = Sort.Direction.DESC) Pageable pageable
     */
    @GetMapping("/posts")
    public String list(
            @RequestParam(value = "page", defaultValue = "1") int page, //현재 페이지
            Model model){

        //페이징
        long totalElementNum = postServiceImpl.getCountPosts(); //전체 게시글 개수 조회
        MyPageble myPageble = new MyPageble(page, totalElementNum);
        List<Post> postList = postServiceImpl.getPagePosts(myPageble);
        PostPagingDto postPagingDto= new PostPagingDto(postList, myPageble);

        log.info("currentBlock={} start={}, end={}, currentPage={}", postPagingDto.getBlock(), postPagingDto.getStartPage(), postPagingDto.getEndPage(), postPagingDto.getNumber());

        model.addAttribute("pagingInfo", postPagingDto);

        return "post/postList";
    }

    /** 게시글 조회
     * - 첨부파일 다운로드 받을 수 있게
     * - 조회수 증가
     * - 작성자만 수정/삭제 버튼 보이게
     */
    @GetMapping("/post/{postId}")
    public String viewPostInfo(@PathVariable("postId") Long postId,
                               @RequestParam(value = "page", defaultValue = "1") int page,
                               Model model){
        Post post = postServiceImpl.viewPost(postId);
        //entity -> dto
        PostInfoDto postInfoDto = new PostInfoDto(post);
        model.addAttribute("post", postInfoDto);
        model.addAttribute("page", page);

        return "post/postInfo";
    }

    /**
     * 첨부파일 다운로드
     * @return null 이면 실패
     */
    @GetMapping("/download/{postId}/{fileId}")
    public ResponseEntity<UrlResource> downloadAttach(@PathVariable Long postId, @PathVariable Long fileId) throws MalformedURLException {

        Post post = postServiceImpl.findOne(postId);
        List<UploadFile> attachFiles = post.getAttachFiles(); //LAZY 로딩

        for (UploadFile file : attachFiles) {
            if(file.getId().equals(fileId)){
                String storeFileName = file.getStoredFileName();
                String uploadFileName = file.getUploadFileName();

                //고객이 저장한 파일 이름으로 다운로드 되도록한다.
                UrlResource resource = new UrlResource("file:" + fileServiceImpl.getFullPath(storeFileName));

                //한글 안깨지도록 인코드 함, 브라우저마다 다름
                String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
                //다운로드: Content-Disposition 헤더에 이 값을 추가해야함
                String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                        .body(resource);
            }
        }
        return null;
    }

    /** 게시글 수정 폼 */
    @GetMapping("/post/{postId}/modify")
    public String modifyPostForm(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
            @PathVariable("postId") Long postId,
            @RequestParam Long writerId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model){

        //권한외 요청일 시
        if(loginMember.getId() != writerId){
            return "redirect:/login";
        }

        //보여줄 원래 내용 찾아서 entity -> dto
        Post post = postServiceImpl.findOne(postId);
        CreatePostDto dto = new CreatePostDto(post);

        model.addAttribute("post", dto);
        model.addAttribute("page", page);

        return "post/modifyPostForm";
    }

    /** 게시글 수정 */
    @PostMapping("/post/{postId}/modify")
    public String modifyPost(
//            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
            @PathVariable("postId") Long postId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @ModelAttribute("post") CreatePostDto createPostDto, BindingResult result,
            RedirectAttributes redirectAttributes) throws IOException {

        //검증확인
        if(result.hasErrors()){
            return "/post/{postId}/modify";
        }

        List<Long> nonDeletedFileId = createPostDto.getNonDeletedFileId();//삭제되지 않고 남은 기존 파일(storedFileList는 왜 null이 될까 ㅠ)
        List<MultipartFile> addFileList = createPostDto.getMultipartFiles(); //수정 시 추가된 파일
        List<UploadFile> dbFileList = fileServiceImpl.findFileList(postId); //DB에 저장된 파일

        log.info("기존에서 남은파일: {}개",nonDeletedFileId != null ?nonDeletedFileId.size():0);
        log.info("추가된파일: {}개", addFileList != null ?addFileList.size():0);
        log.info("DB파일: {}개", dbFileList != null ?dbFileList.size():0);

        //체크 로직-----------------------------
        if(!CollectionUtils.isEmpty(dbFileList)){ //DB에 파일이 최소한 하나 이상 존재O
            //TODO nonDeletedFileId가 비었으면 '모두' 삭제
            if(CollectionUtils.isEmpty(nonDeletedFileId)){
                fileServiceImpl.deleteAllFile(postId);
            }
            else{
                //nonDeletedFileList이 하나 이상 존재할 경우 -> '여기 없는 파일만'(중복되지않는값) DB에서 삭제
                List<UploadFile> deletedIdList  = dbFileList.stream()
                        .filter(dbFile ->
                                nonDeletedFileId.stream().noneMatch(file -> dbFile.getId().equals(dbFile.getId())))
                        .collect(Collectors.toList());

                for (UploadFile file : deletedIdList) {
                    log.info("삭제될 파일:{}",file.getId());
                    fileServiceImpl.deleteFile(file.getId());
                }
            }
        } //DB에 파일이 하나도 존재X -> 당연히 storedFileList도 비어있으니 새로 추가된 파일만 그대로 업로드 하면된다

        //새로 추가된 파일 저장
        List<UploadFile> checkedFileList = checkFiles(addFileList);
        if (checkedFileList == null){
            result.reject("fileUploadFail", PostConst.FILE_SIZE_EXCEEDED);
            return "post/createPostForm";
        }

        //검증 성공 시 게시글 수정
        postServiceImpl.update(
                postId,
                createPostDto.getTitle(),
                createPostDto.getContent(),
                checkedFileList
        );

        redirectAttributes.addAttribute("update_ok", true);

        return "redirect:/posts?page=" + page;
    }

    /** 게시글 삭제 */
    @PostMapping("/post/{postId}/delete")
    public String deletePost(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
            @PathVariable("postId") Long postId,
            @RequestParam Long writerId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            RedirectAttributes redirectAttributes){

        //권한 외 요청일 시
        if(loginMember.getId() != writerId){
            return "redirect:/login";
        }

        postServiceImpl.delete(postId);
        redirectAttributes.addAttribute("delete_ok", true);

        return "redirect:/posts?page=" + page;
    }

    /** 답글(자식) 게시글 작성 폼 */

    /** 답글(자식) 게시글 작성 */
}
