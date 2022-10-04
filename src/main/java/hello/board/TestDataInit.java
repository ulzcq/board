package hello.board;

import hello.board.domain.member.Member;
import hello.board.domain.member.MemberRepository;
import hello.board.domain.post.Post;
import hello.board.domain.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @PostConstruct는 해당 빈의 AOP 적용을 보장하지 않으므로 우회해야한다.
 * 애플리케이션 컨텍스트가 완전히 초기화 된 이벤트를 받아서 호출하는 방법을 사용
 */
@Profile("dev")
@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initDB(){
        initTestMember();
    }

    public void initTestMember() {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        Member member1 = new Member("testid1", passwordEncoder.encode("testpass1"),"유유백서");
        Member member2 = new Member("testid2", passwordEncoder.encode("testpass2"),"카리나");
        Member member3 = new Member("testid3", passwordEncoder.encode("testpass3"),"한번도글안쓴자");
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        Post post1 = new Post("처음 쓰는 글!", "내용이다 불라불라", LocalDateTime.now(), 0);
        Post post2 = new Post("두번째 글", "내용이다 불라불라",LocalDateTime.now(), 0);
        Post post3 = new Post("이건 카리나가 쓴 글", "내용이다 불라불라",LocalDateTime.now(), 0);
        post1.setMember(member1);
        post2.setMember(member1);
        post3.setMember(member2);
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);

        for(int i=0; i<139; i++){
            Post newPost = new Post("[총 개수:139] 페이징 테스트", "제곧내",LocalDateTime.now(), 0);
            newPost.setMember(member2);
            postRepository.save(newPost);
        }

    }
}
