package ppSpring12Task17ClientSide.controller;

import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

/**
 * @author AkiraRokudo on 12.04.2020 in one of sun day
 */
@Controller
@RequestMapping("/")
public class LoginController {

    @GetMapping
    public String getLoginForm() {
        return "login";
    }

    @GetMapping("login/oauth2/authorization")
    public String getLoginForm(OAuth2ClientProperties properties) {
        int i = 4;
        return "login";
    }

//    @GetMapping("/login/oauth2/authorization/google")
//    public String getPageAfterLogin(Principal principal) {
//
//    }
}
