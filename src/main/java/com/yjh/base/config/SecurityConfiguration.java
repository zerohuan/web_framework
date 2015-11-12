package com.yjh.base.config;

import com.yjh.cg.site.service.AuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;

import javax.inject.Inject;

/**
 * Spring security configuration
 *
 * Created by yjh on 15-11-1.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private static final String PREFIX_URL_MANAGE = "/m";

    @Inject
    private AuthenticationService authenticationService;

    @Bean
    protected SessionRegistry sessionRegistryImpl() {
        return new SessionRegistryImpl();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.authenticationProvider(authenticationService);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**", "/m/signup/**", "/m/login/**");
    }

    @Override
    protected void configure(HttpSecurity security) throws Exception {
        security
                .authorizeRequests()
                    .antMatchers(PREFIX_URL_MANAGE + "/student/**", PREFIX_URL_MANAGE + "/user/**",
                            PREFIX_URL_MANAGE + "/course/**").hasAnyAuthority("USER")
                    .anyRequest().authenticated()
                .and().formLogin()
                    .loginPage(PREFIX_URL_MANAGE + "/login").failureUrl(PREFIX_URL_MANAGE + "/login?error")
                    .defaultSuccessUrl(PREFIX_URL_MANAGE + "/student/")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .permitAll()
                .and().logout()
                    .logoutUrl(PREFIX_URL_MANAGE + "/logout").logoutSuccessUrl(PREFIX_URL_MANAGE +
                    "/login?loggedOut")
                    .invalidateHttpSession(true).deleteCookies("JSESSINID")
                    .permitAll()
                //限制用户的会话数量，默认呢行为是再登录时就使现有的会话过期，这里修改为阻止用户再次登录
                .and().sessionManagement()
                    .sessionFixation().changeSessionId()
                    .maximumSessions(1).maxSessionsPreventsLogin(true)
                    .sessionRegistry(sessionRegistryImpl())
                .and().and().csrf().disable()
                //启动remember-me，一周内有效
                .rememberMe().tokenValiditySeconds(7 * 24 * 60 * 60);

    }
}
