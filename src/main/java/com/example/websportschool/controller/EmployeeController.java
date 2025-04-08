package com.example.websportschool.controller;

import com.example.websportschool.entity.UserEntity;
import com.example.websportschool.service.EmployeeService;
import com.example.websportschool.util.JwtUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.List;
import java.util.Optional;

@Controller
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    public String showEmployees(
            @CookieValue(value = "authToken", required = false) String authToken,
            Model model) {
        boolean isAuthenticated = authToken != null && JwtUtil.validateToken(authToken);
        model.addAttribute("isAuthenticated", isAuthenticated);

        // Получаем группировку сотрудников по специализации (чанками по 3 для слайдеров)
        Map<String, List<List<UserEntity>>> groupedEmployeesChunks = employeeService.getEmployeesGroupedBySpecializationInChunks();
        model.addAttribute("groupedEmployeesChunks", groupedEmployeesChunks);
        return "employees";
    }

    @GetMapping("/employees/{id}")
    public String showEmployeeDetail(
            @CookieValue(value = "authToken", required = false) String authToken,
            @PathVariable("id") Long id, Model model) {
        boolean isAuthenticated = authToken != null && JwtUtil.validateToken(authToken);
        model.addAttribute("isAuthenticated", isAuthenticated);

        Optional<UserEntity> employeeOpt = employeeService.getEmployeeById(id);
        if (employeeOpt.isPresent()) {
            model.addAttribute("employee", employeeOpt.get());
            return "employeeDetail";
        }
        return "redirect:/employees";
    }

    @GetMapping("/employee/image/{id}")
    public ResponseEntity<byte[]> getEmployeeImage(@PathVariable("id") Long id) {
        Optional<UserEntity> employeeOpt = employeeService.getEmployeeById(id);
        if (employeeOpt.isPresent() && employeeOpt.get().getImage() != null) {
            byte[] image = employeeOpt.get().getImage();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(image, headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
