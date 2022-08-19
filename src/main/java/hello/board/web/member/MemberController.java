package hello.board.web.member;

import hello.board.SessionConst;
import hello.board.domain.member.Member;
import hello.board.MemberConst;
import hello.board.domain.member.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    private final MemberServiceImpl memberServiceImpl;

    /** 회원 가입 폼 */
    @GetMapping("/members/new")
    public String signUpForm(@ModelAttribute("signUpForm") SignUpMemberDto signUpMemberDto){
        return "members/signUpMemberForm";
    }

    /** 회원 가입
     *  유효성 검사 복습하기 @Validated, BindingResult
     */
    @PostMapping("/members/new")
    public String singUp(@Validated @ModelAttribute("signUpForm") SignUpMemberDto signUpMemberDto, BindingResult result, RedirectAttributes redirectAttributes){
        //1) 검증 에러가 있으면 회원가입 폼으로 다시 돌려보낸다
        if(result.hasErrors()){
            log.info("errors= {}", result);
            return "members/signUpMemberForm";
        }

        //에러가 없으면 회원가입 처리 : dto -> entity
        Member member = signUpMemberDto.toEntity();

        Long savedId = memberServiceImpl.join(member);
        //2) 회원가입 실패 시 회원가입 폼으로 다시 돌려보낸다
        if(savedId == null){
            return "members/signUpMemberForm";
        }

        //회원가입 완료창 띄우기 위한 attribute
        redirectAttributes.addFlashAttribute("result", true);

        return "redirect:/"; //홈 화면으로
    }

    /** 내 정보 조회 및 프로필 수정화면 폼 */
    @GetMapping("/member/{memberId}")
    public String viewModifyProfile(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
            @PathVariable("memberId") Long memberId, Model model){

        if(loginMember.getId() != memberId){
            //다른 멤버 아이디로 url 조회 시 로그인 폼으로
            return "redirect:/login";
        }

        ModifyMemberDto modifyMemberDto = new ModifyMemberDto(loginMember);
        model.addAttribute("member", modifyMemberDto);
        return "members/modifyProfileForm";
    }

    /** 프로필 수정 */
    @PutMapping("/member/{memberId}")
    public String modifyMember(@Validated @ModelAttribute("member") ModifyMemberDto modifyMemberDto, BindingResult result){

        if(result.hasErrors()){
            return "member/modify/{memberId}";
        }

        Member member = memberServiceImpl.findOne(modifyMemberDto.getMemberId());
        member.updateName(modifyMemberDto.getName()); //이름 수정

        return "redirect:/members/modifyProfileForm";
    }

    /** 로그인 화면 폼 */
    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginDto loginDto){
        return "login/loginForm";
    }

    /** 로그인 */
    @PostMapping("/login")
    @ExceptionHandler
    public String login(@Validated @ModelAttribute("loginForm") LoginDto loginDto, BindingResult result,
                        @RequestParam(defaultValue = "/") String redirectURL, HttpServletRequest request){
        //1) 검증 에러가 있으면 로그인 폼으로 다시 돌려보낸다
        if(result.hasErrors()){
            return "login/loginForm";
        }

        //에러가 없으면 로그인
        Member loginMember = memberServiceImpl.login(loginDto.getLoginId(), loginDto.getPassword());

        //2) null 이면 실패하고 로그인 폼으로 다시 돌려보낸다
        if(loginMember == null){
            result.reject("loginFail", MemberConst.INCORRECT_ACCOUNT);
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
