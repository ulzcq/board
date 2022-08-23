package hello.board.web;

import hello.board.SessionConst;
import hello.board.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
public class HomeController {

    /** 홈 화면
     * 로그인 하지 않은 사용자 -> home
     * 로그인 한 사용자 -> 회원용 게시판
     * */
    @GetMapping("/")
    public String Home(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                       Model model){

        //세션 가져오는 건 스프링이 해준다
        //로그인 세션이 없는 사용자는 홈으로 돌려보낸다
        if(loginMember == null){
            return "home";
        }

        //세션이 유지되면 게시판(구:로그인 홈)으로 이동
//        model.addAttribute("member", loginMember);
        return "redirect:/posts";
    }

}
