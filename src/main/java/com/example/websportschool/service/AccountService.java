// Файл: AccountService.java
package com.example.websportschool.service;

import com.example.websportschool.dto.AccountDTO;
import com.example.websportschool.entity.UserEntity;
import com.example.websportschool.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Base64;

@Service
public class AccountService {

    private final UserEntityRepository userRepository;

    @Autowired
    public AccountService(UserEntityRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public AccountDTO convertToDTO(UserEntity user) {
        AccountDTO dto = new AccountDTO();
        dto.setId(user.getId());
        dto.setImage(user.getImage());
        if(user.getImage() != null) {
            String imageBase64 = Base64.getEncoder().encodeToString(user.getImage());
            dto.setImageBase64(imageBase64);
        }
        dto.setSurname(user.getSurname());
        dto.setName(user.getName());
        dto.setPatronymic(user.getPatronymic());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setStatusName(user.getStatusName());
        dto.setSpecialization(user.getSpecialization());
        dto.setWorkExperience(user.getWorkExperience());
        dto.setAdditionalInfo(user.getAdditionalInfo());
        return dto;
    }
}
