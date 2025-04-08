package com.example.websportschool.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String showAdminPanel(Model model) {
        // Если необходимо, можно добавить в модель атрибуты для дальнейшей работы
        return "admin_panel"; // имя представления (шаблона)
    }
}
