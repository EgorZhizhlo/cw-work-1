package com.example.websportschool.service;

import com.example.websportschool.entity.UserEntity;
import com.example.websportschool.repository.UserEntityRepository;
import com.example.websportschool.util.JwtUtil;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AuthService {

    @Autowired
    private UserEntityRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Регистрация пользователя.
     * Если пользователь с таким email уже существует – возвращается сообщение об ошибке.
     * В противном случае пользователь сохраняется и возвращается JWT-токен.
     */
    @Transactional
    public String registerUser(MultipartFile image, String surname, String name, String patronymic,
                               String email, String phoneNumber, String password) throws IOException {
        Optional<UserEntity> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            return "Пользователь с таким email уже существует.";
        }
        String encodedPassword = passwordEncoder.encode(password);
        UserEntity user = new UserEntity();
        if (!image.isEmpty()) {
            try {
                byte[] imageBytes = image.getBytes();
                user.setImage(imageBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            user.setImage(null);
        }

        user.setSurname(surname);
        user.setName(name);
        user.setPatronymic(patronymic);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setPassword(encodedPassword);
        user.setStatusName("USER");


        userRepository.save(user);
        return JwtUtil.generateToken(user);
    }

    @Transactional(readOnly = true)
    public String loginUser(String email, String password) {
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return null;
        }
        UserEntity user = userOptional.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return null;
        }
        return JwtUtil.generateToken(user);
    }
}
