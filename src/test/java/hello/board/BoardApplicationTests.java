package hello.board;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.board.domain.member.Member;
import hello.board.domain.member.QMember;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
class BoardApplicationTests {

	@Autowired EntityManager em;

	@Test
	void contextLoads() {
		Member member = new Member("tid", "tpass", "냠냠");
		em.persist(member);

		JPAQueryFactory queryFactory = new JPAQueryFactory(em);
		QMember qMember = QMember.member; //Querydsl Q타입 동작 확인

		Member result = queryFactory
				.selectFrom(qMember)
				.fetchFirst();
        Assertions.assertThat(result).isEqualTo(member);

		//lombok 동작 확인
		Assertions.assertThat(result.getName()).isEqualTo(member.getName());
	}

}
