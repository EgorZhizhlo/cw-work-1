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

import java.util.List;

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
        if (!isAuth) return "error/403";

        UserEntity user = accountService.getUserById(
                JwtUtil.getUserIdFromToken(token)
        );
        model.addAttribute("role", user.getStatusName());
        if ("trainer".equalsIgnoreCase(user.getStatusName())) {
            model.addAttribute("scheduleList",
                    scheduleService.getScheduleForTrainer(user));
        } else {
            model.addAttribute("scheduleList",
                    scheduleService.getScheduleForStudent(user));
        }
        return "schedule";
    }

    /** Детальная страница занятия с кнопкой «Записаться»/«Отменить» */
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id,
                         Model model,
                         @CookieValue(value = "authToken", required = false) String token) {
        boolean isAuth = token != null && JwtUtil.validateToken(token);
        model.addAttribute("isAuthenticated", isAuth);
        if (!isAuth) return "error/403";

        UserEntity user = accountService.getUserById(
                JwtUtil.getUserIdFromToken(token)
        );
        ScheduleDTO dto = scheduleService.getScheduleDtoById(id);
        boolean signed = scheduleService
                .getScheduleForStudent(user)
                .stream().anyMatch(s -> s.getId().equals(id));

        model.addAttribute("schedule", dto);
        model.addAttribute("isSignedUp", signed);
        return "scheduleDetail";
    }

    /** Обработка записи на занятие */
    @PostMapping("/{id}/signup")
    public String signUp(@PathVariable Long id,
                         @CookieValue("authToken") String token) {
        UserEntity user = accountService.getUserById(
                JwtUtil.getUserIdFromToken(token)
        );
        scheduleService.signUp(id, user);
        return "redirect:/schedule/" + id;
    }

    /** Обработка отмены записи */
    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable Long id,
                         @CookieValue("authToken") String token) {
        UserEntity user = accountService.getUserById(
                JwtUtil.getUserIdFromToken(token)
        );
        scheduleService.cancelSignUp(id, user);
        return "redirect:/schedule/" + id;
    }
}
