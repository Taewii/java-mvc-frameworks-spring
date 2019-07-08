package residentevil.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import residentevil.domain.enums.Authority;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

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
                    .antMatchers("/user/register", "/user/login")
                        .anonymous()
                    .antMatchers("/virus/show")
                        .hasRole("USER")
                    .antMatchers("/virus/add", "/virus/edit", "/virus/delete")
                        .hasRole("MODERATOR")
                    .antMatchers("/api/**")
                        .hasRole("ROOT")
                    .anyRequest()
                        .authenticated()
                    .and()
                .formLogin()
                    .loginPage("/user/login")
                    .defaultSuccessUrl("/")
                    .and()
                .logout()
                    .logoutUrl("/user/logout")
                    .logoutSuccessUrl("/");

    }
}
