package productshop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import productshop.domain.enums.Authority;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userService;
    private final CsrfTokenRepository csrfTokenRepository;

    @Autowired
    public WebSecurityConfig(@Qualifier("UserServiceImpl") UserDetailsService userService,
                             CsrfTokenRepository csrfTokenRepository) {
        this.userService = userService;
        this.csrfTokenRepository = csrfTokenRepository;
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
                    .antMatchers("/users/register", "/users/login")
                        .anonymous()
                    .antMatchers("/home")
                        .hasRole(Authority.ROOT.name())
                    .anyRequest()
                        .authenticated()
                    .and()
                .formLogin()
                    .loginPage("/users/login")
                    .defaultSuccessUrl("/")
                    .and()
                .rememberMe()
                    .userDetailsService(userService)
                    .tokenValiditySeconds(60 * 60 * 24 * 14) // 2 weeks
                    .key("remember-me")
                    .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/");
    }
}
