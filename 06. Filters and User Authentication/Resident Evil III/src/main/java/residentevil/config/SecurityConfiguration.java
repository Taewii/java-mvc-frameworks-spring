package residentevil.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import residentevil.domain.enums.Authority;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final CsrfTokenRepository csrfTokenRepository;
    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfiguration(@Qualifier(value = "UserServiceImpl") UserDetailsService userDetailsService,
                                 CsrfTokenRepository csrfTokenRepository) {
        this.csrfTokenRepository = csrfTokenRepository;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                    .disable()
                .csrf()
                    .csrfTokenRepository(csrfTokenRepository)
                    .and()
                .authorizeRequests()
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                        .permitAll()
                    .antMatchers("/")
                        .permitAll()
                    .antMatchers("/user/register", "/user/login")
                        .anonymous()
                    .antMatchers("/virus/show")
                        .hasRole(Authority.USER.name())
                    .antMatchers("/virus/add", "/virus/edit/**", "/virus/delete/**")
                        .hasRole(Authority.MODERATOR.name())
                    .antMatchers("/user/users", "/api/users")
                        .hasRole(Authority.ADMIN.name())
                    .anyRequest()
                        .authenticated()
                    .and()
                .formLogin()
                    .loginPage("/user/login")
                    .defaultSuccessUrl("/")
                    .and()
                .rememberMe()
                    .userDetailsService(userDetailsService)
                    .tokenValiditySeconds(60 * 60 * 24 * 14) // 2 weeks
                    .key("remember-me")
                    .and()
                .logout()
                    .logoutUrl("/user/logout")
                    .logoutSuccessUrl("/");
    }
}
