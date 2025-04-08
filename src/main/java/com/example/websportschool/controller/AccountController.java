// Файл: AccountController.java
package com.example.websportschool.controller;

import com.example.websportschool.dto.AccountDTO;
import com.example.websportschool.entity.UserEntity;
import com.example.websportschool.service.AccountService;
import com.example.websportschool.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public String getAccountPage(@CookieValue(value = "authToken", required = false) String authToken, Model model) {
        boolean isAuthenticated = authToken != null && JwtUtil.validateToken(authToken);
        model.addAttribute("isAuthenticated", isAuthenticated);
        if (!isAuthenticated) {
            return "error/403"; // шаблон ошибки доступа
        }
        Long userId = JwtUtil.getUserIdFromToken(authToken);
        if (userId == null) {
            return "error/403";
        }
        UserEntity user = accountService.getUserById(userId);
        if (user == null || !( "employee".equalsIgnoreCase(user.getStatusName()) || "user".equalsIgnoreCase(user.getStatusName()) )) {
            return "error/403";
        }
        AccountDTO accountDTO = accountService.convertToDTO(user);
        model.addAttribute("account", accountDTO);
        return "account"; // имя HTML шаблона account.html
    }
}
