package hello.board.web.member;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class SignUpMemberDto {

    @NotBlank
    @Size(min =6, max = 20)
    private String loginId;

    @NotBlank
    @Size(min =8, max = 15)
    private String password;

    @NotBlank
    @Size(min =2, max = 20)
    private String name;
}
