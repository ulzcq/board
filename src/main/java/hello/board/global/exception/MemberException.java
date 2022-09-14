package hello.board.global.exception;

import org.springframework.http.HttpStatus;

public class MemberException extends RuntimeException{

    private int errorCode;
    private String errorMessage;
    private HttpStatus httpStatus;

    public MemberException(String message) {
        errorMessage = message;
    }

    @Override
    public String getMessage() {
        return this.getMessage();
    }
}
