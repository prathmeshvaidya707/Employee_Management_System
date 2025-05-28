package com.prathmesh.New_project.dao;

import com.prathmesh.New_project.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    // add method to sort by last name
    Optional<Employee> findByEmail(String email);
    public List<Employee> findAllByOrderByLastnameAsc();
}
