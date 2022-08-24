package hello.board.domain.member;

import hello.board.web.member.ModifyMemberDto;
import hello.board.web.member.ModifyPasswordDto;

import java.util.List;

public interface MemberService {
    Long join(Member member); //회원가입

    boolean validateDuplicateMember(String loginId); //중복 ID 검증

    Member login(String loginId, String password); //로그인

    void update(Long memberId, ModifyMemberDto modifyMemberdto); //회원 정보 수정

    boolean updatePassword(Long memberId, ModifyPasswordDto modifyPasswordDto); //비밀번호 변경

    /** 조회 */
    List<Member> findMembers(); //전체 회원 조회

    Member findOne(Long memberId); //회원 1명 조회
}
