package hello.board;

import hello.board.web.interceptor.LogInterceptor;
import hello.board.web.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 싱글톤 컨테이너 사용: @Configuration
 * - 스프링 빈은 항상 무상태(stateless) 설계를 해야함
 * - 즉 공유필드 X (https://www.notion.so/4-6fcf998b44834c9d9541181cc70e2cda 참고)
 *
 * 의존 관계 자동 주입: @ComponentScan
 * - basePackages 지정은 생략, 설정 정보 클래스의 위치를 프로젝트 최상단에 두는 것이 관례
 *     - 그리고 프로젝트 메인 설정 정보는 프로젝트를 대표하는 정보이기 때문에 프로젝트 시작 루트 위치에 두는 것이 좋다.
 * - 최근 스프링 부트도 이 방법을 기본으로 제공
 *     - 스프링 부트의 대표 시작 정보인 @SpringBootApplication를 프로젝트시작 루트위치에 두는 것이 관례
 *     - 이 설정안에 바로 @ComponentScan 이 들어있다!
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css","/*.ico","/error");

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/","/members/new","/login","/logout","/css/**","/*.ico","/error");
    }

}
