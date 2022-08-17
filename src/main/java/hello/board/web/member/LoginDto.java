package hello.board.web.member;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class LoginDto {

    @NotBlank
    private String loginId;

    @NotBlank
    private String password;
}
