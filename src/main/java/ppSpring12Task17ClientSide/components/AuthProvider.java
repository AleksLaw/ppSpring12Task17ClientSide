package ppSpring12Task17ClientSide.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ppSpring12Task17ClientSide.model.UserData;
import ppSpring12Task17ClientSide.service.UserService;

import java.util.Collection;

/**
 * @author AkiraRokudo on 08.05.2020 in one of sun day
 */
@Component
public class AuthProvider implements AuthenticationProvider {

    //На самом деле класс не особо и нужен - по логину у нас вход воспрещен, только по почте.
    //реализован вместо дефолтного практики и гибкости ради.
    //UPD. Ну и еще чтоб в дальнейщем вырубать здесь нахрен доступ по дефолтному паролю

    private UserDetailsService userService;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthProvider(UserDetailsService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        UserData user = (UserData) userService.loadUserByUsername(username);

        if (user != null && (user.getUsername().equals(username) || user.getLogin().equals(username))) {
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new BadCredentialsException("Wrong password");
            }

            Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

            return new UsernamePasswordAuthenticationToken(user, password, authorities);
        } else
            throw new BadCredentialsException("Username not found");
    }

    public UserDetailsService getUserService() {
        return userService;
    }

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public boolean supports(Class<?> arg) {
        return true;
    }
}
