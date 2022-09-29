package hello.board.global.exception;

import org.springframework.http.HttpStatus;

public interface BaseExceptionType {

    String getMessage();

    HttpStatus getHttpStatus();

    int getErrorCode();
}
