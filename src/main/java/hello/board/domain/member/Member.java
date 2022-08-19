package hello.board.domain.member;

import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

/**
 * 엔티티에는 가급적 Setter를 사용하지 않는다
 * TODO: 나중에 프로필 사진 추가
 */
@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, length = 20, unique = true)
    private String loginId; //로그인 ID

    @Column(nullable = false)
    private String password; //비밀번호

    @Column(nullable = false, length = 20)
    private String name; //이름

    public Member(String loginId, String password, String name) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
    }

    /** update 메서드 */

    public void updateName(String name) {
        this.name = name;
    }

    public void updatePassword(PasswordEncoder passwordEncoder, String password) {
        this.password = passwordEncoder.encode(password);
    }

    /** 패스워드 암호화/확인 메서드 */

    public void encodePassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(password);
    }

    public boolean matchPassword(PasswordEncoder passwordEncoder, String inputPassword){
        return passwordEncoder.matches(inputPassword, this.password);
    }

}
