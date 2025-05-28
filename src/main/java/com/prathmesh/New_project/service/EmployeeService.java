package com.prathmesh.New_project.service;

import com.prathmesh.New_project.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    List<Employee> findAll();
    Employee findById(int theId);
    Employee save(Employee theEmployee);
    void deleteById(int theId);
    Optional<Employee> findByEmail(String email);

}
