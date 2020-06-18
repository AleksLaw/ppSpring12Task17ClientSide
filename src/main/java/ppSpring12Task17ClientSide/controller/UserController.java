package ppSpring12Task17ClientSide.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author AkiraRokudo on 08.04.2020 in one of sun day
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping
    public String getUserInfo() {
        return "user";
    }

}
