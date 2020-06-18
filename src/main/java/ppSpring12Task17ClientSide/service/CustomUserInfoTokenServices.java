package ppSpring12Task17ClientSide.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedAuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedPrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ppSpring12Task17ClientSide.dto.UserDto;
import ppSpring12Task17ClientSide.model.RoleData;
import ppSpring12Task17ClientSide.model.UserData;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author AkiraRokudo on 08.05.2020 in one of sun day
 */

@Service
public class CustomUserInfoTokenServices implements ResourceServerTokenServices {

    @Value("${google.resource.userInfoUri}")
    private String userInfoEndpointUrl;

    @Value("${google.client.clientId}")
    private String clientId;

    @Value("${default.server.user.password}")
    private String defaultPassword;

    private OAuth2RestOperations restTemplate;
    private UserService userService;
    private PasswordEncoder passwordEncoder; //оставлен для красоты. Т.к. у нас нет определения бина енкодера

    @Autowired
    public CustomUserInfoTokenServices(OAuth2RestTemplate googleTemplate, UserService userService,PasswordEncoder passwordEncoder) {
        this.restTemplate = googleTemplate;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken)
            throws AuthenticationException, InvalidTokenException {
        Map<String, Object> map = getMap(this.userInfoEndpointUrl, accessToken);

        if (map.containsKey("sub")) {
            String googleId = (String) map.get("sub");
            String firstName = (String) map.get("given_name");
            String lastName = (String) map.get("family_name");
            String email = (String) map.get("email");
            UserDto userDto = userService.getUserByLogin(email);
            //User user = userRepo.findByGoogleUsername(googleUsername);
            if (userDto.getId() == null) {  //значит такого у нас нет.
                userDto = new UserDto();
                //укажем айдишник от гугла,
                //пароль по дефолту
                userDto.setLogin(email);
                userDto.setFirstName(firstName);
                userDto.setLastName(lastName);
                userDto.setMoney(0L); //это возраст. Все никак не доходит до рефакторинга
                userDto.setPassword(defaultPassword);
                userDto.setGoogleId(googleId);
                Set<String> userRole = new HashSet<>();
                userRole.add("User");
                userDto.setRoles(userRole);
                userService.addObject(userDto);
            } else {
                //проверим что у нас есть его гугл айдишник и отпустим
                if (userDto.getGoogleId() == null || userDto.getGoogleId().trim().equals("")) {
                    //тут мы проставим ему айдишник
                    userDto.setGoogleId(googleId);
                    userService.editObject(userDto);
                }
            }

        }

        if (map.containsKey("error")) {
            throw new InvalidTokenException(accessToken);
        }
        return extractAuthentication(map);
    }

    /**
     * Метод для извлечения аутентификации - юзера, ролей и полномочий(пароль)
     * @param map карта с параметрами от гугла
     * @return
     */
    private OAuth2Authentication extractAuthentication(Map<String, Object> map) {
        String login = map.get("email").toString();//единственный параметр от гугла который нам нужен
        UserDto userDto = userService.getUserByLogin(login);
        Set<RoleData> roles = new HashSet<>();
        userDto.getRoles().stream().forEach(rs -> roles.add(new RoleData(rs)));
        UserData user = new UserData(userDto.getLogin(), userDto.getPassword(), roles);
        Object principal = user;
        List<GrantedAuthority> authorities = new ArrayList<>(roles);

        //упакуем извлеченные параметры
        OAuth2Request request = new OAuth2Request(null, this.clientId, null, true, null,
                null, null, null, null);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                principal, userDto.getPassword(), authorities);
        token.setDetails(map);
        return new OAuth2Authentication(request, token);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        throw new UnsupportedOperationException("Not supported: read access token");
    }

    /**
     * Ключевой метод для получения параметров от гугла
     * @param path
     * @param accessToken
     * @return
     */
    private Map<String, Object> getMap(String path, String accessToken) {
        try {
            OAuth2RestOperations restTemplate = this.restTemplate;
            if (restTemplate == null) {
                BaseOAuth2ProtectedResourceDetails resource = new BaseOAuth2ProtectedResourceDetails();
                resource.setClientId(this.clientId);
                restTemplate = new OAuth2RestTemplate(resource);
            }
            OAuth2AccessToken existingToken = restTemplate.getOAuth2ClientContext()
                    .getAccessToken();
            if (existingToken == null || !accessToken.equals(existingToken.getValue())) {
                DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(
                        accessToken);
                token.setTokenType("Bearer");
                restTemplate.getOAuth2ClientContext().setAccessToken(token);
            }
            return restTemplate.getForEntity(path, Map.class).getBody();
        } catch (Exception ex) {
            return Collections.singletonMap("error",
                    "Could not fetch user details");
        }
    }
}
