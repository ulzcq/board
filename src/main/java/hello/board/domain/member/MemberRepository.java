package hello.board.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByLoginId(String loginId); //로그인ID로 회원 조회
//    List<Member> findByName(String name); //이름으로 회원 조회
}

