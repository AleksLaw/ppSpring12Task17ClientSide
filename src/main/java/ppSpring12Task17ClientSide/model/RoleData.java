package ppSpring12Task17ClientSide.model;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author AkiraRokudo on 30.04.2020 in one of sun day
 */
public class RoleData implements GrantedAuthority {

    private String name;

    public RoleData() {
    }

    public RoleData(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return getPrefix() + getName();
    }

    public String getPrefix() {
        return "ROLE_";
    }
}
