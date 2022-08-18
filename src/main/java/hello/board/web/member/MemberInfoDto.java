package hello.board.web.member;

import hello.board.domain.member.Member;
import lombok.Data;

@Data
public class MemberInfoDto {
    private Long memberId;
    private String loginId; //로그인 ID
    private String name; //이름

    public MemberInfoDto(Member member) {
        this.memberId = member.getId();
        this.loginId = member.getLoginId();
        this.name = member.getName();
    }
}
