// Файл: ScheduleController.java
package com.example.websportschool.controller;

import com.example.websportschool.dto.ScheduleDTO;
import com.example.websportschool.entity.UserEntity;
import com.example.websportschool.service.ScheduleService;
import com.example.websportschool.service.AccountService;
import com.example.websportschool.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final AccountService accountService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService, AccountService accountService) {
        this.scheduleService = scheduleService;
        this.accountService = accountService;
    }

    @GetMapping
    public String getSchedulePage(@CookieValue(value = "authToken", required = false) String authToken, Model model) {
        boolean isAuthenticated = authToken != null && JwtUtil.validateToken(authToken);
        model.addAttribute("isAuthenticated", isAuthenticated);
        if (!isAuthenticated) {
            return "error/403";
        }
        Long userId = JwtUtil.getUserIdFromToken(authToken);
        if (userId == null) {
            return "error/403";
        }
        UserEntity user = accountService.getUserById(userId);
        if (user == null || !( "employee".equalsIgnoreCase(user.getStatusName()) || "user".equalsIgnoreCase(user.getStatusName()) )) {
            return "error/403";
        }
        List<ScheduleDTO> scheduleList = scheduleService.getScheduleForUser(user);
        model.addAttribute("scheduleList", scheduleList);
        return "schedule"; // имя HTML шаблона schedule.html
    }
}
