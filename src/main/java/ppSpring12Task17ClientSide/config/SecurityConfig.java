package ppSpring12Task17ClientSide.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ppSpring12Task17ClientSide.components.AuthProvider;
import ppSpring12Task17ClientSide.config.handler.LoginSuccessHandler;
import ppSpring12Task17ClientSide.service.CustomUserInfoTokenServices;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
@EnableOAuth2Client
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private AuthProvider authProvider;
    private CustomUserInfoTokenServices tokenServices;
    private OAuth2RestOperations restTemplate;

    @Autowired
    public SecurityConfig(AuthProvider authProvider, CustomUserInfoTokenServices tokenServices, OAuth2RestTemplate restTemplate) {
        this.authProvider = authProvider;
        this.tokenServices = tokenServices;
        this.restTemplate = restTemplate;
    }

    @Bean
    @ConfigurationProperties("google.resource")
    public ResourceServerProperties googleResource() {
        return new ResourceServerProperties();
    }

    @Bean
    public FilterRegistrationBean oAuth2ClientFilterRegistration(OAuth2ClientContextFilter oAuth2ClientContextFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(oAuth2ClientContextFilter);
        registration.setOrder(-100);//указываем самым первым
        return registration;
    }

    private Filter ssoFilter() {
        //возможно эти огороды и не нужны. Ведь все это - игры с созданием фильтра оаус
        OAuth2ClientAuthenticationProcessingFilter googleFilter = new OAuth2ClientAuthenticationProcessingFilter("/login/oauth2/authorization/google");
        googleFilter.setRestTemplate(restTemplate);
        googleFilter.setTokenServices(tokenServices);
        googleFilter.setAuthenticationSuccessHandler(new LoginSuccessHandler());
        return googleFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProvider);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //csrf, login and logout
        http
                .formLogin()
                // указываем страницу с формой логина
                .loginPage("/")
                //указываем логику обработки при логине
                .successHandler(new LoginSuccessHandler())
                // указываем action с формы логина
                //.loginProcessingUrl("/")
                // даем доступ к форме логина всем
                .permitAll()
                .and()
                .logout()
                // разрешаем делать логаут всем
                .permitAll()
                // указываем URL логаута
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                // указываем URL при удачном логауте
                .logoutSuccessUrl("/?logout")
                //выклчаем кроссдоменную секьюрность (на этапе обучения неважна)
                .and()
                .csrf().disable();

        http
                // делаем страницу регистрации недоступной для авторизированных пользователей
                .authorizeRequests()
                //страницы аутентификаци доступна всем
                .antMatchers("/").anonymous()
                // защищенные URL
                .antMatchers("/admin/**").access("hasAnyRole('Admin')").anyRequest().authenticated()
                .and()
                .addFilterBefore(ssoFilter(), UsernamePasswordAuthenticationFilter.class);

    }

}
