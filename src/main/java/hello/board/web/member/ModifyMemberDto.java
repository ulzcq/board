package hello.board.web.member;

import hello.board.domain.member.Member;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class ModifyMemberDto {

    private Long memberId;
    private String loginId; //로그인 ID

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z가-힣]{2,20}+$")
    private String name; //이름

    //TODO: 기본 생성자 없으면 org.springframework.beans.BeanInstantiationException 에러남.. 왜그렇지??
    public ModifyMemberDto(){}

    public ModifyMemberDto(Member member) {
        this.memberId = member.getId();
        this.loginId = member.getLoginId();
        this.name = member.getName();
    }
}
