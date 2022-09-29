package hello.board.global.exception;

public class CustomException extends BaseException {

    private BaseExceptionType exceptionType;

    //생성자로 생성하는 순간 exceptionType이 필요하도록
    public CustomException(BaseExceptionType exceptionType){
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return exceptionType;
    }
}
