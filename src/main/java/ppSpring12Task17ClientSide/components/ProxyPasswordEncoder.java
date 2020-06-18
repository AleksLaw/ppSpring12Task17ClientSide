package ppSpring12Task17ClientSide.components;

import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author AkiraRokudo on 08.05.2020 in one of sun day
 */
@Component
public class ProxyPasswordEncoder implements PasswordEncoder {

    PasswordEncoder pe = NoOpPasswordEncoder.getInstance();

    @Override
    public String encode(CharSequence rawPassword) {
        return pe.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return pe.matches(rawPassword, encodedPassword);
    }
}
