package ppSpring12Task17ClientSide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ppSpring12Task17ClientSide.components.ServerUrl;
import ppSpring12Task17ClientSide.dto.UserDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author AkiraRokudo on 29.02.2020 in one of sun day
 */
@Service
public class UserServiceImpl implements UserService {

    private ServerUrl serverUrl;
    private HttpHeaders basicAuthHeaders;

    @Autowired
    public UserServiceImpl(HttpHeaders basicAuthHeaders, ServerUrl serverUrl) {
        this.basicAuthHeaders = basicAuthHeaders;
        this.serverUrl = serverUrl;
    }

    @Override
    public UserDto addObject(UserDto userDto) {
        try {
            HttpEntity<String> request = new HttpEntity(userDto,basicAuthHeaders);
            String url = serverUrl.getServerUrl() + "/admin/create";
            //создали шаблон
            RestTemplate userTemplate = new RestTemplate();
            //послали и получили ответ
            ResponseEntity<?> response = userTemplate.exchange(url, HttpMethod.POST, request,Object.class);
            HttpStatus status = response.getStatusCode();
            if(HttpStatus.OK != status) {
                throw new IllegalStateException("Exception on serverside");
            }
        } catch (RestClientException e) {
            e.printStackTrace();
        }
        return null; //а потому что не задумывалось
    }

    @Override
    public void editObject(UserDto userDto) {
        try {
            HttpEntity<String> request = new HttpEntity(userDto,basicAuthHeaders);
            String url = serverUrl.getServerUrl() + "/admin/edit";
            //создали шаблон
            RestTemplate userTemplate = new RestTemplate();

            //послали и получили ответ
            ResponseEntity<?> response = userTemplate.exchange(url, HttpMethod.PUT, request,Object.class);
            HttpStatus status = response.getStatusCode();
            if(HttpStatus.OK != status) {
                throw new IllegalStateException("Exception on serverside");
            }
        } catch (RestClientException e) {
            e.printStackTrace();
        }

    }

    /**
     * @param id искомого юзера
     * @return null если дали null или несуществующий айдишник
     */
    @Override
    public UserDto getObject(Long id) {
        UserDto userDto = null;
        try {
            HttpEntity<String> request = new HttpEntity(basicAuthHeaders);
            String url = serverUrl.getServerUrl() + "/admin/edituser/"+id;
            //создали шаблон
            RestTemplate userTemplate = new RestTemplate();
            //послали и получили ответ
            ResponseEntity<UserDto> response = userTemplate.exchange(url, HttpMethod.GET, request,UserDto.class);
            userDto = response.getBody();
        } catch (RestClientException e) {
            e.printStackTrace();
        }

        return userDto;
    }

    /**
     * @param login искомого юзера
     * @return null если дали null или несуществующий айдишник
     */
    @Override
    public UserDto getUserByLogin(String login) {
        return getObjectByParameters("login", login);
    }

    /**
     * @param id если null - Ничего не удалит
     */
    @Override
    public void removeObject(Long id) {
        UserDto deleteUser = getObject(id);
        if (deleteUser != null) {
            removeObject(deleteUser);
        }
    }

    @Override
    public void removeObject(UserDto userDto) {
        try {
            HttpEntity<String> request = new HttpEntity(userDto,basicAuthHeaders);
            String url = serverUrl.getServerUrl() + "/admin/delete";
            //создали шаблон
            RestTemplate userTemplate = new RestTemplate();
            ResponseEntity<?> response = userTemplate.exchange(url, HttpMethod.DELETE, request,Object.class);
            HttpStatus status = response.getStatusCode();
            if(HttpStatus.OK != status) {
                throw new IllegalStateException("Exception on serverside");
            }
        } catch (RestClientException e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserDto getObjectByParameters(String paramName, String paramValue) {
        UserDto userDto = new UserDto();

        //по хорошему надо слать реквест с 2 параметрами, и их читать. но мы схитрим и ифом отсечем неподдерживаемые
        if ("login".equals(paramName)) {
            try {

                HttpEntity<String> request = new HttpEntity(paramValue,basicAuthHeaders);
                String url = serverUrl.getServerUrl() + "/userInfo";
                //создали шаблон
                RestTemplate userTemplate = new RestTemplate();
                //послали и получили ответ
                ResponseEntity<UserDto[]> response = userTemplate.exchange(url, HttpMethod.POST, request,UserDto[].class);
                userDto = response.getBody()[0];
            } catch (RestClientException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("можно искать только по логину");
        }

        return userDto;
    }

    @Override
    public List<UserDto> getAll() {
        List<UserDto> users = new ArrayList<>();
            try {

                HttpEntity<String> request = new HttpEntity(basicAuthHeaders);
                String url = serverUrl.getServerUrl() + "/admin/allusers";
                //создали шаблон
                RestTemplate userTemplate = new RestTemplate();
                //послали и получили ответ
                ResponseEntity<UserDto[]> response = userTemplate.exchange(url, HttpMethod.GET, request,UserDto[].class);
                users = Arrays.asList(response.getBody());
            } catch (RestClientException e) {
                e.printStackTrace();
            }

        return users;
    }

    @Override
    public Boolean isAdmin(UserDto user) {
        return user.getRoles().contains("Admin");
    }
}
