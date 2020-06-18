package ppSpring12Task17ClientSide.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

/**
 * @author AkiraRokudo on 14.03.2020 in one of sun day
 */
@Controller
@RequestMapping("/admin")
public class AdminController {


    //TODO: переписать весь пакет в один бин для шорткатных отдач вьюх

    @GetMapping
    public String getUsers(Principal userP, ModelMap model) {
        return "admin";
    }

}
