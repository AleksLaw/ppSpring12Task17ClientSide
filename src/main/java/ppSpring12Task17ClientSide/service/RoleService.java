package ppSpring12Task17ClientSide.service;


import ppSpring12Task17ClientSide.dto.RoleDto;

/**
 * @author AkiraRokudo on 07.04.2020 in one of sun day
 */
public interface RoleService extends CrudService<RoleDto, RoleDto> {

    RoleDto getRoleByName(String name);

}
