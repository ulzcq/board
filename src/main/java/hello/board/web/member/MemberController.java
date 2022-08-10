package hello.board.web.member;

import hello.board.SessionConst;
import hello.board.domain.member.Member;
import hello.board.domain.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Controller -> Service -> Repository
 */
@Slf4j
@Controller
@RequiredArgsConstructor // final 붙은 객체 의존관계 주입
public class MemberController {

    private final MemberService memberService;

    /** 회원 가입 폼 */
    @GetMapping("/members/new")
    public String createForm(@ModelAttribute("form") SignUpMemberDto signUpMemberDto){
        return "members/signUpMemberForm";
    }

    /** 회원 가입
     *  유효성 검사 복습하기 @Validated, BindingResult
     */
    @PostMapping("/members/new")
    public String create(@Validated @ModelAttribute("form") SignUpMemberDto signUpMemberDto, BindingResult result, RedirectAttributes redirectAttributes){
        //1) 검증 에러가 있으면 회원가입 폼으로 다시 돌려보낸다
        if(result.hasErrors()){
            log.info("errors= {}", result);
            return "members/signUpMemberForm";
        }

        //에러가 없으면 회원가입 처리 : form -> entity
        Member member = new Member(
                signUpMemberDto.getLoginId(),
                signUpMemberDto.getPassword(),
                signUpMemberDto.getName()
        );

        Long savedId = memberService.join(member);
        //2) 회원가입 실패 시 회원가입 폼으로 다시 돌려보낸다
        if(savedId == null){
            return "members/signUpMemberForm";
        }

        //회원가입 완료창 띄우기 위한 attribute
        redirectAttributes.addFlashAttribute("result", true);

        return "redirect:/"; //홈 화면으로
    }

    /** 로그인 화면 폼 */
    @GetMapping("/login")
    public String loginForm(@ModelAttribute("form") LoginDto loginDto){
        return "login/loginForm";
    }

    /** 로그인 */
    @PostMapping("/login")
    public String login(@Validated @ModelAttribute("form") LoginDto loginDto, BindingResult result,
                        @RequestParam(defaultValue = "/") String redirectURL, HttpServletRequest request){
        //1) 검증 에러가 있으면 로그인 폼으로 다시 돌려보낸다
        if(result.hasErrors()){
            return "login/loginForm";
        }

        //에러가 없으면 로그인
        Member loginMember = memberService.login(loginDto.getLoginId(), loginDto.getPassword());

        //2) null 이면 실패하고 로그인 폼으로 다시 돌려보낸다
        if(loginMember == null){
            result.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다");
            return "login/loginForm";
        }

        //로그인 성공 처리
        HttpSession session = request.getSession(); //세션이 있으면 반환, 없으면 신규로 생성
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember); //세션에 로그인 회원 정보 보관

        return "redirect:"+ redirectURL; //loginForm > form action 태그
    }

    /** 로그아웃 */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate(); //세션 파기
        }
        return "redirect:";
    }
}
