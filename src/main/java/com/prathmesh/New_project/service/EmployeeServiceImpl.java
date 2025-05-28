package com.prathmesh.New_project.service;

import com.prathmesh.New_project.dao.EmployeeRepository;
import com.prathmesh.New_project.entity.Employee;
import com.prathmesh.New_project.exception.DuplicateEmailException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAllByOrderByLastnameAsc();
    }

    @Override
    @Transactional
    public Employee findById(int theId) {
        return employeeRepository.findById(theId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }


    @Override
    public Employee save(Employee theEmployee) {
        Optional<Employee> existing = employeeRepository.findByEmail(theEmployee.getEmail());
        if (existing.isPresent() && existing.get().getId() != theEmployee.getId()) {
            throw new DuplicateEmailException("Email already exists: " + theEmployee.getEmail());
        }
        return employeeRepository.save(theEmployee);
    }


    @Override
    @Transactional
    public void deleteById(int theId) {
        employeeRepository.deleteById(theId);
    }

    @Override
    public Optional<Employee> findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

}
