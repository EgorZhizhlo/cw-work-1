package com.example.websportschool.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RegistrationDTO {
    private String surname;
    private String name;
    private String patronymic;
    private String email;
    private String password;
    private MultipartFile image;
}
