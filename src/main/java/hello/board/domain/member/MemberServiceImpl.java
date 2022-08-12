package hello.board.domain.member;

import hello.board.web.member.MemberInfoDto;
import hello.board.web.member.SignUpMemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final H2MemberRepository memberRepository;

    /** 회원 가입
     * @return null 이면 회원가입 실패
     * */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    /** 중복 회원 검증 */
    public void validateDuplicateMember(Member member){
        //DB에 Id를 가진 회원이 있는지 확인
        Member findMember = memberRepository.findByLoginId(member.getLoginId());
        if(findMember != null){
            throw new IllegalStateException("이미 존재하는 아이디 입니다."); //TODO: 예외처리
        }
    }

    /** 로그인
     *  @return null 이면 로그인 실패
     */
    @Transactional
    public Member login(String loginId, String password){
        return memberRepository.findByLoginId(loginId);
    }

    /** 회원 정보 수정 */
    @Override
    public Member update(Member member) {
        //TODO
    }

    /** 내 정보 조회*/
    @Override
    public Member getMyInfo() {
        //TODO
    }

    /** 전체 회원 조회 */
    @Transactional
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /** 회원 1명 조회 */
    @Transactional
    public Member findOne(Long memberId){
        return memberRepository.findById(memberId);
    }

}
