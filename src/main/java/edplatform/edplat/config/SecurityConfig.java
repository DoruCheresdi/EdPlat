package edplatform.edplat.config;

import edplatform.edplat.entities.users.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // requires authentication for every request except for the login page:
        http.authorizeRequests()
                .mvcMatchers("/course/enroll")
                    .access("not @securityAuthorizationChecker.checkCourseOwnerByCourseId(authentication,request)")
                .mvcMatchers("/assignment/new", "/course/delete")
                    .access("@securityAuthorizationChecker.checkCourseOwnerByCourseId(authentication,request)")
                .mvcMatchers("/assignment/edit", "/assignment/change_description")
                    .access("@securityAuthorizationChecker.checkCourseOwnerByAssignmentId(authentication,request)")
                .mvcMatchers("/course/edit", "/course/process_img_edit",
                        "/course/change_name", "/course/change_description")
                    .access("@securityAuthorizationChecker.checkCourseOwnerById(authentication,request)")
                .mvcMatchers("/register", "/process_register").permitAll()
                .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .usernameParameter("email")
                    .defaultSuccessUrl("/user/courses")
                    .permitAll()
                .and()
                    .logout().logoutSuccessUrl("/").permitAll();
    }
}
