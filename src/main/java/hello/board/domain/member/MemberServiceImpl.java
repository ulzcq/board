package hello.board.domain.member;

import hello.board.global.exception.CustomException;
import hello.board.global.exception.CustomExceptionType;
import hello.board.web.member.ModifyMemberDto;
import hello.board.web.member.ModifyPasswordDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 시스템 예외상황이 아닌 비지니스 예외상황 시(일반적으로 IO 등이 아닌 개발자가 예상가능한 상황)
 * 가급적 예외를 터트리지 않고, 앞에서 한번 체크
 * 다만 심각한 비즈니스 상황에서는 예외를 발생시킨다
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final H2MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /** 회원 가입
     * @return null 이면 회원가입 실패
     * */
    @Transactional
    public Long join(Member member) {
        boolean state = validateDuplicateMember(member.getLoginId());// 중복  ID 검증
        if(state == false){
            return null;
        }
        member.encodePassword(passwordEncoder); // 패스워드 암호화

        return memberRepository.save(member);
    }

    /** 중복 회원 검증
     * @return true : 성공
     * @return false : 로그인 Id 중복
     */
    public boolean validateDuplicateMember(String loginId){
        //DB에 Id를 가진 회원이 있는지 확인
        Member findMember = memberRepository.findByLoginId(loginId);
        if(findMember != null){
            return false;
        }
        return true;
    }

    /** 회원 정보 수정 */
    @Transactional
    @Override
    public void update(Long memberId, ModifyMemberDto modifyMemberdto) {
        Member member = memberRepository.findById(memberId);
        if(member == null) throw new CustomException(CustomExceptionType.NOT_FOUND_MEMBER);
        member.updateName(modifyMemberdto.getName()); //이름 변경
    }

    /** 비밀번호 변경
     * @return true : 성공
     * @return false : 비밀번호 불일치
     */
    @Transactional
    @Override
    public boolean updatePassword(Long memberId, ModifyPasswordDto modifyPasswordDto) {
        Member member = memberRepository.findById(memberId);

        //입력한 현재 비밀번호가 일치하는지 확인
        if(!member.matchPassword(passwordEncoder, modifyPasswordDto.getCurrentPassword())){
            return false;
        }

        member.updatePassword(passwordEncoder, modifyPasswordDto.getChangePassword()); //비밀번호 변경
        return true;
    }

    /** 로그인
     *  @return null 이면 로그인 실패
     */
    @Transactional
    public Member login(String loginId, String password){
        Member member = memberRepository.findByLoginId(loginId);
        //패스워드 일치 확인
        if(member != null){
            if(!member.matchPassword(passwordEncoder, password)){
                return null;
            }
        }
        return member;
    }

    /** 전체 회원 조회 */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /** 회원 1명 조회 */
    public Member findOne(Long memberId){
        return memberRepository.findById(memberId);
    }

}
