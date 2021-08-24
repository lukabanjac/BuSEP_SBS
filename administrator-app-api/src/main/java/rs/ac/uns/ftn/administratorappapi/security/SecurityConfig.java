//package com.gk.JDBCSpringSecurity.JDBCAuthentication.Config;
package rs.ac.uns.ftn.administratorappapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import rs.ac.uns.ftn.administratorappapi.security.TokenHelper;
import rs.ac.uns.ftn.administratorappapi.security.auth.RestAuthenticationEntryPoint;
import rs.ac.uns.ftn.administratorappapi.service.impl.CustomUserDetailsService;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService jwtUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    //Neautorizovani pristup zastcenim resursima
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Autowired
    public void configureGlobal( AuthenticationManagerBuilder auth ) throws Exception {
        auth.userDetailsService( jwtUserDetailsService )
                .passwordEncoder( passwordEncoder() );
    }

    @Autowired
    TokenHelper tokenHelper;




    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                // Most Restrictive To Least Restrictive in terms of order
                .antMatchers("static/css", "static/js").permitAll() //Static files permit for all users
                .antMatchers("/admin").hasRole("ADMIN") // .hasAnyRole("USER","ADMIN")
                .antMatchers("/user").hasAnyRole("USER", "ADMIN")
                .antMatchers("api/user/register").hasAuthority("SUPERADMIN")
                .antMatchers("/").permitAll()
                .and().formLogin().and().cors().and().csrf().disable();;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedMethods("*");
            }
        };
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance();
//    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                HttpMethod.POST,
                "/api/user/login"
        );
        web.ignoring().antMatchers(
                HttpMethod.GET,
                "/",
                "/webjars/**",
                "/*.html",
                "/favicon.ico",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js",
                "/**/*.jpg"
        );

    }

}