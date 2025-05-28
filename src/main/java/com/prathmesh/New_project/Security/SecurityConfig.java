package com.prathmesh.New_project.Security;

import com.prathmesh.New_project.dao.EmployeeRepository;
import com.prathmesh.New_project.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(employeeRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/employees/list-employee").hasAnyRole("EMPLOYEE", "MANAGER", "ADMIN")
                        .requestMatchers("/employees/save").hasAnyRole("MANAGER", "ADMIN")
                        .requestMatchers("/employees/delete").hasRole("ADMIN")
                        .requestMatchers("/employees/showFormForAdd", "/employees/showFormForUpdate").hasAnyRole("MANAGER", "ADMIN")
                        .anyRequest().permitAll()
                )
                .exceptionHandling(auth -> auth.accessDeniedPage("/access-denied"))
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/authenticateTheUser")
                        .successHandler(successHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .permitAll()
                );
        return http.build();
    }

    public static class CustomUserDetailsService implements UserDetailsService {

        private final EmployeeRepository employeeRepository;

        public CustomUserDetailsService(EmployeeRepository employeeRepository) {
            this.employeeRepository = employeeRepository;
        }

        @Override
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
            Employee employee = employeeRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Invalid Email"));

            return User.builder()
                    .username(employee.getEmail())
                    .password(employee.getPassword())
                    .roles(employee.getRole().getRoleName().replace("ROLE_", ""))
                    .build();
        }
    }
}
