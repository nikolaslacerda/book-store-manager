package com.server.bookstoremanager.config;

import com.server.bookstoremanager.user.enums.Role;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String USERS_API_URL = "/api/v1/users/**";
    private static final String PUBLISHERS_API_URL = "/api/v1/publishers/**";
    private static final String AUTHORS_API_URL = "/api/v1/authors/**";
    private static final String BOOKS_API_URL = "/api/v1/books/**";
    private static final String H2_CONSOLE_URL = "/h2-console/**";
    private static final String SWAGGER_URL = "/swagger-ui.html";
    private static final String ROLE_ADMIN = Role.ADMIN.getDescription();
    private static final String ROLE_USER = Role.USER.getDescription();

    private static final String[] SWAGGER_RESOURCES = {
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
    };

    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private UserDetailsService userDetailsService;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeRequests()
                .antMatchers(USERS_API_URL, H2_CONSOLE_URL, SWAGGER_URL).permitAll()
                .antMatchers(PUBLISHERS_API_URL, AUTHORS_API_URL, BOOKS_API_URL).hasAnyRole(ROLE_ADMIN)
                .antMatchers(BOOKS_API_URL).hasAnyRole(ROLE_ADMIN, ROLE_USER)
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.headers().frameOptions().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(SWAGGER_RESOURCES);
    }
}
