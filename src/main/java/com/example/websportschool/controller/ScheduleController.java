package com.example.websportschool.controller;

import com.example.websportschool.dto.ScheduleDTO;
import com.example.websportschool.entity.UserEntity;
import com.example.websportschool.service.ScheduleService;
import com.example.websportschool.service.AccountService;
import com.example.websportschool.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final AccountService accountService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService,
                              AccountService accountService) {
        this.scheduleService = scheduleService;
        this.accountService = accountService;
    }

    /** Список всех занятий (для студента или тренера) */
    @GetMapping
    public String list(Model model,
                       @CookieValue(value = "authToken", required = false) String token) {
        boolean isAuth = token != null && JwtUtil.validateToken(token);
        model.addAttribute("isAuthenticated", isAuth);
        if (!isAuth) {
            return "error/403";
        }

        UserEntity user = accountService.getUserById(
                JwtUtil.getUserIdFromToken(token)
        );
        String role = user.getStatusName().toUpperCase();
        model.addAttribute("role", role);

        if ("EMPLOYEE".equals(role)) {
            // тренер видит только свои занятия
            model.addAttribute("scheduleList",
                    scheduleService.getScheduleForTrainer(user));
        } else {
            // студент – свои записи (ADMIN здесь тоже попадёт, но у него их не будет)
            model.addAttribute("scheduleList",
                    scheduleService.getScheduleForStudent(user));
        }

        return "schedule";
    }

    /** Детальная страница занятия */
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id,
                         Model model,
                         @CookieValue(value = "authToken", required = false) String token) {
        boolean isAuth = token != null && JwtUtil.validateToken(token);
        model.addAttribute("isAuthenticated", isAuth);
        if (!isAuth) {
            return "error/403";
        }

        UserEntity user = accountService.getUserById(
                JwtUtil.getUserIdFromToken(token)
        );
        String role = user.getStatusName().toUpperCase();
        model.addAttribute("role", role);

        // запрещаем запись/отмену для EMPLOYEE и ADMIN
        boolean isPrivileged = "EMPLOYEE".equals(role) || "ADMIN".equals(role);
        model.addAttribute("canSignUp", !isPrivileged);

        // DTO занятия
        ScheduleDTO dto = scheduleService.getScheduleDtoById(id);
        model.addAttribute("schedule", dto);

        // проверяем, записан ли студент (только если ему можно)
        boolean isSignedUp = false;
        if (!isPrivileged) {
            isSignedUp = scheduleService
                    .getScheduleForStudent(user)
                    .stream()
                    .anyMatch(s -> s.getId().equals(id));
        }
        model.addAttribute("isSignedUp", isSignedUp);

        return "scheduleDetail";
    }

    /** Обработка записи на занятие (только для не-EMPLOYEE и не-ADMIN) */
    @PostMapping("/{id}/signup")
    public String signUp(@PathVariable Long id,
                         @CookieValue("authToken") String token) {
        UserEntity user = accountService.getUserById(
                JwtUtil.getUserIdFromToken(token)
        );
        String role = user.getStatusName().toUpperCase();
        if (!"EMPLOYEE".equals(role) && !"ADMIN".equals(role)) {
            scheduleService.signUp(id, user);
        }
        return "redirect:/schedule/" + id;
    }

    /** Обработка отмены записи (только для не-EMPLOYEE и не-ADMIN) */
    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable Long id,
                         @CookieValue("authToken") String token) {
        UserEntity user = accountService.getUserById(
                JwtUtil.getUserIdFromToken(token)
        );
        String role = user.getStatusName().toUpperCase();
        if (!"EMPLOYEE".equals(role) && !"ADMIN".equals(role)) {
            scheduleService.cancelSignUp(id, user);
        }
        return "redirect:/schedule/" + id;
    }
}
