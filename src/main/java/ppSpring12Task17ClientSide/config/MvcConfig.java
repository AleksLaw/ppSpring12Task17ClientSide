package ppSpring12Task17ClientSide.config;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;

import java.util.Arrays;

/**
 * @author AkiraRokudo on 04.05.2020 in one of sun day
 */
@Configuration
public class MvcConfig {

//    @Bean
//    RestOperations restTemplateBuilder(RestTemplateBuilder restTemplateBuilder) {
//        return restTemplateBuilder.basicAuthorization("ADMIN", "ADMIN").build();
//    }


   // private OAuth2ClientContext oAuth2ClientContext;

    @Bean
    public HttpHeaders headersWithBaseAuth() {
        String plainCreds = "ADMIN:ADMIN";
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic " + base64Creds);
        return headers;
    }

    @Bean
    @ConfigurationProperties("google.client")
    public AuthorizationCodeResourceDetails google() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    public OAuth2RestTemplate getGoogleTemplate(@Qualifier("oauth2ClientContext") OAuth2ClientContext oAuth2ClientContext) {
        OAuth2RestTemplate googleTemplate = new OAuth2RestTemplate(google(), oAuth2ClientContext);
        return googleTemplate;
    }

}
