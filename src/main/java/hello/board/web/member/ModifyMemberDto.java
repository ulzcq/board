package hello.board.web.member;

import hello.board.domain.member.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
public class ModifyMemberDto {

    private Long memberId;
    private String loginId; //로그인 ID

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z가-힣]{2,20}+$")
    private String name; //이름

    public ModifyMemberDto(Member member) {
        this.memberId = member.getId();
        this.loginId = member.getLoginId();
        this.name = member.getName();
    }
}
