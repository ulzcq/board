package hello.board.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = "hello.board")
@Slf4j
public class CustomErrorController implements ErrorController {

    @ExceptionHandler(BaseException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public String handleMember(BaseException ex, Model model){
        if(ex.getExceptionType().getHttpStatus() == HttpStatus.NOT_FOUND){
            String message = ex.getExceptionType().getMessage();
            log.error("MemberException: {} ", message);

            model.addAttribute("message", message);

            return "errors/404-custom";
        }

        return "error";
    }

    //그외에 놓친 예외들
    @ExceptionHandler(Exception.class)
    public String handleAll(Exception ex){
        log.error("Exception: {}", ex);

        return "error";
    }

}
