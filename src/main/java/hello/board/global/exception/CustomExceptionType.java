package hello.board.global.exception;

import org.springframework.http.HttpStatus;

public enum CustomExceptionType implements BaseExceptionType {
    NOT_FOUND_MEMBER("해당 회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND, 100),
    NOT_FOUND_POST("해당 게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND, 201);

    private String message;
    private HttpStatus httpStatus;
    private int errorCode;

    CustomExceptionType(String message, HttpStatus httpStatus, int errorCode) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    @Override
    public int getErrorCode() {
        return this.errorCode;
    }
}
