package hello.board.domain.post;

import hello.board.domain.file.UploadFile;
import hello.board.domain.member.Member;
import hello.board.domain.member.MemberRepository;
import hello.board.global.exception.CustomException;
import hello.board.global.exception.CustomExceptionType;
import hello.board.web.post.PostCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    /** 게시글 등록
     * @return null 이면 실패
     */
    @Transactional
    @Override
    public Post create(Long memberId, List<UploadFile> attachFiles, PostCreateDto postCreateDto) {
        Post post = postCreateDto.toEntity();

        //연관관계 엔티티 조회 및 설정
        Member writer = memberRepository.findById(memberId).orElse(null);
        if(writer == null){
            return null;
        }

        post.setMember(writer);
        if(!CollectionUtils.isEmpty(attachFiles)){
            for (UploadFile attachFile : attachFiles) {
                if(attachFile != null){
                    attachFile.setPost(post);
                }
            }
        }

        return postRepository.save(post);
    }

    /** 게시글 수정 */
    @Transactional
    @Override
    public void update(Long postId, String title, String content, List<UploadFile> attachFiles) {
        //영속상태의 엔티티를 찾아오면 값 변경 시 커밋 때 자동반영
        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(CustomExceptionType.NOT_FOUND_POST));

        //첨부파일이 있으면 연관관계 설정
        if(!CollectionUtils.isEmpty(attachFiles)){
            for (UploadFile attachFile : attachFiles) {
                if(attachFile != null){
                    attachFile.setPost(findPost);
                }
            }
        }

        findPost.change(title, content, attachFiles);

    }

    /** 게시글 삭제 */
    @Transactional
    @Override
    public void delete(Long postId) {
        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(CustomExceptionType.NOT_FOUND_POST));

        postRepository.delete(findPost);
    }

    /** 게시글 한 개 찾기*/
    @Override
    public Post findOne(Long postId) {
        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(CustomExceptionType.NOT_FOUND_POST));
        return findPost;
    }

    /** 게시글 조회
     * - 조회수 변경
     */
    @Transactional
    @Override
    public Post viewPost(Long postId) {
        Post findPost = postRepository.findWithMemberById(postId);
        if(findPost == null) throw new CustomException(CustomExceptionType.NOT_FOUND_POST);
        findPost.cntViews();
        return findPost;
    }

    /** 게시글 리스트 조회 (게시판)
     * - 페이징
     */
    @Override
    public Page<Post> searchPagePosts(PostSearchCondition postSearchCondition, Pageable pageable) {
        //페이징 인덱스+1 처리 했던걸 다시 -1 해준다
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() -1);
        pageable = PageRequest.of(page, 10);
        return postRepository.searchPage(postSearchCondition, pageable);
        //위임만 하기 때문에 이런 경우는 컨트롤러에서 바로 불러도 괜찮다고 함(선택사항)
    }
}
