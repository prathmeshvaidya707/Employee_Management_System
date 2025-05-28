package com.prathmesh.New_project.Controller;

import com.prathmesh.New_project.entity.Employee;
import com.prathmesh.New_project.exception.DuplicateEmailException;
import com.prathmesh.New_project.service.EmployeeService;
import com.prathmesh.New_project.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RoleService roleService;


    @GetMapping("/list")
    public String listEmployees(Model model) {
        model.addAttribute("employees", employeeService.findAll());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // Email of the logged-in user

        Optional<Employee> loggedInUser = employeeService.findByEmail(email);
        loggedInUser.ifPresent(user -> model.addAttribute("loggedInUser", user));

        return "list-employee";
    }


    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("roles", roleService.findAll());
        return "employee-form";
    }

    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute("employee") Employee employee,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            employeeService.save(employee);
            return "redirect:/employees/list";
        } catch (DuplicateEmailException e) {
            model.addAttribute("roles", roleService.findAll());
            model.addAttribute("employee", employee);
            model.addAttribute("emailError", e.getMessage());
            return "employee-form";
        }
    }


    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("employeeId") int id, Model model) {
        model.addAttribute("employee", employeeService.findById(id));
        model.addAttribute("roles", roleService.findAll());
        return "employee-form";
    }

    @GetMapping("/delete")
    public String deleteEmployee(@RequestParam("employeeId") int id) {
        employeeService.deleteById(id);
        return "redirect:/employees/list";
    }

    // For EMPLOYEE role redirection
    @GetMapping("/list-employee")
    public String redirectToList() {
        return "redirect:/employees/list";
    }

    // For ADMIN redirection
    @GetMapping("/FormForDelete")
    public String showFormForDelete(Model model) {
        model.addAttribute("employees", employeeService.findAll());
        return "list-employee"; // Create this HTML page
    }

    // For MANAGER redirection
    @GetMapping("/FormForAddUpdate")
    public String showFormForAddUpdate(Model model) {
        model.addAttribute("employee", new Employee());
        return "list-employee"; // Reuse the form
    }



}
