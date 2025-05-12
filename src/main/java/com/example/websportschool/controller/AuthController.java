package com.example.websportschool.controller;

import com.example.websportschool.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    // Отображение страницы регистрации по URL /registry
    @GetMapping("/registry")
    public String showRegistryPage() {
        return "registry"; // возвращает шаблон registry.html
    }

    // Отображение страницы авторизации по URL /login
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // возвращает шаблон login.html
    }

    // Обработка регистрации
    @PostMapping("/registry")
    public String registerUser(
            @RequestParam("image") MultipartFile image,
            @RequestParam("surname") String surname,
            @RequestParam("name") String name,
            @RequestParam("patronymic") String patronymic,
            @RequestParam("email") String email,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("password") String password,
            HttpServletResponse response,
            Model model) throws IOException {

        String token = authService.registerUser(image, surname, name, patronymic, email, phoneNumber, password);
        if (token.startsWith("Пользователь")) { // например, "Пользователь с таким email уже существует."
            model.addAttribute("error", token);
            return "registry"; // возврат страницы регистрации с сообщением об ошибке
        }

        // При успешной регистрации устанавливаем cookie с токеном
        Cookie authCookie = new Cookie("authToken", token);
        authCookie.setHttpOnly(true);
        authCookie.setPath("/");
        response.addCookie(authCookie);

        return "redirect:/"; // редирект на главную страницу
    }

    // Обработка авторизации (входа)
    @PostMapping("/login")
    public String loginUser(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpServletResponse response,
            Model model) {

        String token = authService.loginUser(email, password);
        if (token == null) {
            model.addAttribute("error", "Неверный email или пароль.");
            return "login"; // возврат страницы авторизации с сообщением об ошибке
        }

        // Успешный вход – установка cookie с токеном
        Cookie authCookie = new Cookie("authToken", token);
        authCookie.setHttpOnly(true);
        authCookie.setPath("/");
        response.addCookie(authCookie);

        return "redirect:/"; // редирект на главную страницу
    }

    // Обработка выхода (logout)
    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        // Удаляем cookie authToken
        Cookie cookie = new Cookie("authToken", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // удаляется сразу
        response.addCookie(cookie);

        return "redirect:/login"; // редирект на страницу логина
    }
}
