package hello.board.web.member;

import hello.board.domain.member.Member;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class SignUpMemberDto {

    @Pattern(regexp = "^[a-z0-9-_]{6,20}+$")
    private String loginId;

    @Pattern(regexp = "^[a-zA-Z0-9`~!@#$%^&*()-_=+)]{8,15}+$")
    private String password;

    @Pattern(regexp = "^[a-zA-Z가-힣]{2,20}+$")
    private String name;

    public Member toEntity(){
        return new Member(loginId, password, name);
    }
}
