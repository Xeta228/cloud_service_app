package ru.baron.cloudapp.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.baron.cloudapp.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService service;

    public SecurityConfig(UserService service) {
        this.service = service;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/","registration","/static/**").permitAll()
                        .anyRequest().authenticated()
                ).rememberMe().and()
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/files",true)
                )
                .logout((logout) -> logout.logoutSuccessUrl("/login").permitAll().deleteCookies());

        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        //should be changed to some normal password encoder later.
        auth.userDetailsService(service).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }


}
