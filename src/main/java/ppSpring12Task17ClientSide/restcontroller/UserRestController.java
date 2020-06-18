package ppSpring12Task17ClientSide.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ppSpring12Task17ClientSide.dto.UserDto;
import ppSpring12Task17ClientSide.service.UserService;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

/**
 * @author AkiraRokudo on 26.04.2020 in one of sun day
 */
@RestController
public class UserRestController {

    private UserService userService;
    //содержит методы для получения
    /*
    1. списка всех пользователей(вызывается при загрузке и при каждом запросе на него влияющем - апдейт, делит, криейт
    2. данных пользователя - при удалении либо редактировании, для открытия модалки
    3. удаление пользователя
    4. редактирование пользователя
     */

    @Autowired
    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/allusers")
    public ResponseEntity<?> getAllUsers() {
        List<UserDto> users = userService.getAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/userInfo")
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        UserDto uPrincipal = userService.getUserByLogin(principal.getName());
        return ResponseEntity.ok(Collections.singletonList(uPrincipal));
    }

    @GetMapping("/admin/edituser/{id}")
    public ResponseEntity<?> getUsersWithEditing(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getObject(id));
    }

    @GetMapping("/user/principal")
    public ResponseEntity<?> getPrincipalEmailAndRoles(Principal userP) {
        UserDto principal = userService.getUserByLogin(userP.getName());
        UserDto user = new UserDto();
        user.setLogin(principal.getLogin());
        user.setRoles(principal.getRoles());
        return ResponseEntity.ok(user);
    }


    @PostMapping("/admin/create")
    public ResponseEntity<?> addUser(@RequestBody UserDto userDto) {
        userService.addObject(userDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/admin/edit")
    public ResponseEntity<?> editUser(@RequestBody UserDto userDto) {
        userService.editObject(userDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/admin/delete")
    public ResponseEntity<?> deleteUser(@RequestBody UserDto userDto) {
        userService.removeObject(userDto.getId()); // ибо косяк может быть с селектнутыми ролями
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
