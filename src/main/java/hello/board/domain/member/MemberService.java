package hello.board.domain.member;

import hello.board.web.member.ModifyMemberDto;

import java.util.List;

public interface MemberService {
    Long join(Member member); //회원가입

    void validateDuplicateMember(Member member); //중복 회원 검증

    Member login(String loginId, String password); //로그인

    void update(Long memberId, ModifyMemberDto modifyMemberdto); //회원 정보 수정

    Member getMyInfo(Long memberId); //내 정보(회원) 조회

    /** 관리자 조회 */
    List<Member> findMembers(); //전체 회원 조회

    Member findOne(Long memberId); //회원 1명 조회
}
