package productshop.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import productshop.domain.enums.Authority;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                    .disable()
                .csrf()
                    .disable()
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
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/");
    }
}
