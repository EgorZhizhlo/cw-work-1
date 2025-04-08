package com.example.websportschool.admin.controller;

import com.example.websportschool.entity.UserEntity;
import com.example.websportschool.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/admin/users")
public class UserAdminController {

    @Autowired
    private UserEntityRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Просмотр списка пользователей
    @GetMapping()
    public String listUsers(Model model) {
        List<UserEntity> users = userRepository.findAll(Sort.by("id"));
        Map<Long, String> userImages = new HashMap<>();
        for (UserEntity user : users) {
            if (user.getImage() != null) {
                String base64Image = Base64.getEncoder().encodeToString(user.getImage());
                userImages.put(user.getId(), base64Image);
            }
        }
        model.addAttribute("users", users);
        model.addAttribute("userImages", userImages);
        return "admin/users/list";
    }

    // Переход к форме создания нового пользователя
    @GetMapping("/new")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new UserEntity());
        return "admin/users/form";
    }

    // Переход к форме редактирования пользователя
    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable("id") Long id, Model model) {
        Optional<UserEntity> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            model.addAttribute("user", userOpt.get());
            return "admin/users/form";
        } else {
            return "redirect:/admin/users";
        }
    }

    // Сохранение нового пользователя (создание)
    @PostMapping("/create")
    public String createUser(@ModelAttribute("user") UserEntity user,
                             @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        // Кодирование пароля, если он задан
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        // Обработка файла изображения, если он загружен
        if (!imageFile.isEmpty()) {
            user.setImage(imageFile.getBytes());
        }
        userRepository.save(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") UserEntity user,
                             @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        Optional<UserEntity> userOpt = userRepository.findById(user.getId());
        if (userOpt.isPresent()) {
            UserEntity existingUser = userOpt.get();

            existingUser.setName(user.getName());
            existingUser.setSurname(user.getSurname());
            existingUser.setPatronymic(user.getEmail());
            existingUser.setEmail(user.getEmail());
            existingUser.setPhoneNumber(user.getPhoneNumber());
            existingUser.setStatusName(user.getStatusName());
            if (Objects.equals(existingUser.getStatusName(), "EMPLOYEE")) {
                existingUser.setSpecialization(user.getSpecialization());
                existingUser.setWorkExperience(user.getWorkExperience());
                existingUser.setAdditionalInfo(user.getAdditionalInfo());
            }
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            // Если загружено новое изображение, обновляем его
            if (!imageFile.isEmpty()) {
                existingUser.setImage(imageFile.getBytes());
            }
            userRepository.save(existingUser);
        }
        return "redirect:/admin/users";
    }

    // Удаление пользователя
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }
}
