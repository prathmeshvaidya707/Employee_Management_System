package com.prathmesh.New_project.Security;

import com.prathmesh.New_project.dao.EmployeeRepository;
import com.prathmesh.New_project.entity.Employee;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderRunner {

    @Bean
    public CommandLineRunner encodeExistingPasswords(EmployeeRepository employeeRepository,
                                                     PasswordEncoder passwordEncoder) {
        return args -> {
            for (Employee emp : employeeRepository.findAll()) {
                String rawPassword = emp.getPassword();
                if (!rawPassword.startsWith("$2a$")) { // Not BCrypt encoded
                    emp.setPassword(passwordEncoder.encode(rawPassword));
                    employeeRepository.save(emp);
                    System.out.println("Encoded password for: " + emp.getEmail());
                }
            }
        };
    }
}

