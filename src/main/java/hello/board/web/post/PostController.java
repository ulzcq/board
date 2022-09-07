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
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    /** 게시글 삭제 */
    @PostMapping("/post/{postId}/delete")
    public String deletePost(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
            @PathVariable("postId") Long postId,
            @RequestParam Long writerId,
            RedirectAttributes redirectAttributes){

        //권한 외 요청일 시
        if(loginMember.getId() != writerId){
            return "redirect:/login";
        }

        postServiceImpl.delete(postId);
        redirectAttributes.addAttribute("delete_ok", true);

        return "redirect:/posts";
    }

    /** 답글(자식) 게시글 작성 폼 */

    /** 답글(자식) 게시글 작성 */
}
