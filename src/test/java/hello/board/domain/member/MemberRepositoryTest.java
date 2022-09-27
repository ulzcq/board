package hello.board.domain.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired H2MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void testMember(){
        Member member = new Member("id123","pass","memberA");
        Long savedId = memberRepository.save(member);
        Member findMember = memberRepository.findById(savedId);

        assertThat(findMember.getId()).isEqualTo(member.getId()); //Id가 같은지 비교
        assertThat(findMember.getName()).isEqualTo(member.getName()); //이름 같은지 비교

        assertThat(findMember).isEqualTo(member); //JPA 엔티티 동일성 보장
    }
}