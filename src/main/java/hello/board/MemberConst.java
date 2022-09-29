package hello.board;

public interface MemberConst {
    String USERID_ALREADY_EXIST = "이미 존재하는 아이디입니다.";//회원가입 - 아이디 중복
    String INCORRECT_ACCOUNT = "아이디 또는 비밀번호를 잘못 입력했습니다.\n 입력하신 내용을 다시 확인해주세요."; //로그인 글로벌 에러
    String INCORRECT_PASSWORD = "비밀번호가 일치하지 않습니다."; //비밀번호 변경 - 현재 비밀번호 불일치
}
