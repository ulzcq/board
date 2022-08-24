package hello.board.web.member;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class ModifyPasswordDto {

    @NotBlank
    private String currentPassword; //현재 비밀번호

    @Pattern(regexp = "^[a-zA-Z0-9`~!@#$%^&*()-_=+)]{8,15}+$")
    private String changePassword; //바꿀 비밀번호(입력받은 것)
}
