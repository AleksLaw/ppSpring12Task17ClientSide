package ppSpring12Task17ClientSide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ppSpring12Task17ClientSide.components.ServerUrl;
import ppSpring12Task17ClientSide.dto.RoleDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author AkiraRokudo on 06.04.2020 in one of sun day
 */
@Service
public class RoleServiceImpl implements RoleService {

    //Шаблон для получения определенной роли по имени
    private ServerUrl serverUrl;
    private HttpHeaders basicAuthHeaders;

    @Autowired
    public RoleServiceImpl(HttpHeaders basicAuthHeaders, ServerUrl serverUrl) {
        this.basicAuthHeaders = basicAuthHeaders;
        this.serverUrl = serverUrl;
    }

    @Override
    public RoleDto addObject(RoleDto role) {
        throw new UnsupportedOperationException("Низя. Идите лесом, или к разработчику");
    }

    @Override
    public void editObject(RoleDto role) {
        throw new UnsupportedOperationException("Низя. Идите лесом, или к разработчику");
    }

    @Override
    public RoleDto getObject(Long id) {
        throw new UnsupportedOperationException("Низя. Идите лесом, или к разработчику");
    }

    @Override
    public RoleDto getRoleByName(String name) {
        return getObjectByParameters("name" , name);
    }

    @Override
    public void removeObject(Long id) {
        throw new UnsupportedOperationException("Низя. Идите лесом, или к разработчику");
    }

    @Override
    public void removeObject(RoleDto role) {
        throw new UnsupportedOperationException("Низя. Идите лесом, или к разработчику");
    }

    @Override
    public RoleDto getObjectByParameters(String paramName, String paramValue) {
        throw new UnsupportedOperationException("Низя. Идите лесом, или к разработчику");
    }

    @Override
    public List<RoleDto> getAll() {
        List<RoleDto> roles = new ArrayList<>();
        try {
            HttpEntity<String> request = new HttpEntity(basicAuthHeaders);
            String url = serverUrl.getServerUrl() + "/admin/roles";
            RestTemplate userTemplate = new RestTemplate();
            //послали и получили ответ
            ResponseEntity<RoleDto[]> response = userTemplate.exchange(url, HttpMethod.GET, request,RoleDto[].class);
            roles = Arrays.asList(response.getBody());
        } catch (RestClientException e) {
            e.printStackTrace();
        }

        return roles;
    }
}
