package hello.board.domain.post;

import hello.board.domain.file.UploadFile;
import hello.board.domain.member.H2MemberRepository;
import hello.board.domain.member.Member;
import hello.board.web.post.MyPageble;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService{

    private final H2PostRepository postRepository;
    private final H2MemberRepository memberRepository;

    /** 게시글 등록
     * @return null 이면 실패
     */
    @Transactional
    @Override
    public Long create(Long memberId, List<UploadFile> attachFiles, Post post) {
        //연관관계 엔티티 조회 및 설정
        Member writer = memberRepository.findById(memberId);
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
        Post findPost = postRepository.findById(postId);

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
        Post findPost = postRepository.findById(postId);
        postRepository.delete(findPost);
    }

    /** 게시글 한 개 찾기*/
    @Override
    public Post findOne(Long postId) {
        return postRepository.findById(postId);
    }

    /** 게시글 조회
     * - 조회수 변경
     */
    @Transactional
    @Override
    public Post viewPost(Long postId) {
        Post findPost = postRepository.findById(postId);
        findPost.cntViews();
        return findPost;
    }

    /** 게시글 리스트 조회 (게시판) */
    @Override
    public List<Post> getPagePosts(MyPageble myPageble) {
        return postRepository.findListWithMember(myPageble);
        //위임만 하기 때문에 이런 경우는 컨트롤러에서 바로 불러도 괜찮다고 함(선택사항)
    }

    @Override
    public long getCountPosts() {
        return postRepository.findAllCnt();
    }
}
