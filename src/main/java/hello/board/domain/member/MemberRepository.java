package hello.board.domain.member;

import java.util.List;

public interface MemberRepository {

    Long save(Member member); //회원 저장

    Member findById(Long id); //id로 회원 조회

    Member findByLoginId(String loginId); //로그인ID로 회원 조회

    List<Member> findByName(String name); //이름으로 회원 조회

    List<Member> findAll(); //모든 회원 조회

}

