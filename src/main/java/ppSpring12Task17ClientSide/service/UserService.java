package ppSpring12Task17ClientSide.service;

import ppSpring12Task17ClientSide.dto.UserDto;

/**
 * @author AkiraRokudo on 07.04.2020 in one of sun day
 */
public interface UserService extends CrudService<UserDto, UserDto> {

    UserDto getUserByLogin(String login);

    Boolean isAdmin(UserDto user);
}
