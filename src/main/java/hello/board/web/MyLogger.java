package hello.board.web;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.UUID;

/**
 * 웹 스코프 - request 스코프 학습용 코드

 * - 웹과 관련된 부분은 컨트롤러까지만 사용해야 한다.
 * - 서비스 계층은 웹 기술에 종속되지 않고, 가급적 순수하게 유지하는 것이 유지보수 관점에서 좋다.
 * - 사실 공통처리이므로 로깅은 컨트롤러보다는 스프링 인터셉터나 서블릿 필터를 활용하는 것이 좋다
 * TODO: 스프링 인터셉터로 바꾸기
 *
 * 프록시 : 진짜 객체 조회를 꼭 필요한 시점까지 지연처리
 * - MyLogger의 가짜 프록시 클래스를 만들어두고 HTTP request와 상관없이 가짜 프록시 클래스를 다른 빈에 미리 주입
 * - 가짜 프록시 객체는 요청이 오면 그때 내부에서 진짜 빈을 요청하는 위임 로직이 들어있다.
 * - 적용 대상이 클래스면 TARGET_CLASS 를 선택, 인터페이스면 INTERFACES 를 선택
 */
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS) //프록시 사용
public class MyLogger {

    private String uuid; //이 빈은 HTTP 요청 당 하나씩 생성되므로, uuid를 저장해두면 다른 HTTP 요청과 구분할 수 있다.
    private String requestURL; //requestURL 정보도 추가로 넣어서 어떤 URL을 요청해서 남은 로그인지 확인

    //requestURL은 이 빈이 생성되는 시점에는 알 수 없으므로, 외부에서 setter로 입력 받는다
    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public void log(String message){
        System.out.println("[" + uuid + "]" + "[" + requestURL +"] " + message);
    }

    /** 빈 생명주기 초기화 콜백 메서드 사용, 의존관계 주입이 끝나면 호출 */
    @PostConstruct //이 빈이 생성되는 시점에 uuid를 생성해서 저장해둔다.
    public void init(){
        uuid = UUID.randomUUID().toString();
        System.out.println("[" + uuid + "]" + "request scope bean create:" + this);
    }

    /** 빈 생명주기 소멸전 콜백 메서드 사용, 스프링 컨테이너가 종료 직전 호출 */
    @PreDestroy //이 빈이 소멸되는 시점에 종료 메시지를 남긴다.
    public void close(){
        System.out.println("[" + uuid + "]" + "request scope bean close:" + this);
    }
}
