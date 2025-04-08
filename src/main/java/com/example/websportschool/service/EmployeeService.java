package com.example.websportschool.service;

import com.example.websportschool.entity.UserEntity;
import com.example.websportschool.repository.UserEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class EmployeeService {

    private final UserEntityRepository userRepository;

    public EmployeeService(UserEntityRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Извлечение сотрудников и группировка по specialization (заменяем null на "Не указана")
    public Map<String, List<UserEntity>> getEmployeesGroupedBySpecialization() {
        List<UserEntity> employees = userRepository.findByStatusName("EMPLOYEE");
        return employees.stream()
                .collect(Collectors.groupingBy(e -> e.getSpecialization() != null ? e.getSpecialization() : "Общая практика"));
    }

    // Деление списка сотрудников на чанки по 3 элемента
    public Map<String, List<List<UserEntity>>> getEmployeesGroupedBySpecializationInChunks() {
        Map<String, List<UserEntity>> grouped = getEmployeesGroupedBySpecialization();
        Map<String, List<List<UserEntity>>> result = new HashMap<>();
        for (Map.Entry<String, List<UserEntity>> entry : grouped.entrySet()) {
            result.put(entry.getKey(), chunkList(entry.getValue(), 3));
        }
        return result;
    }

    private List<List<UserEntity>> chunkList(List<UserEntity> list, int chunkSize) {
        List<List<UserEntity>> chunks = new ArrayList<>();
        for (int i = 0; i < list.size(); i += chunkSize) {
            chunks.add(list.subList(i, Math.min(i + chunkSize, list.size())));
        }
        return chunks;
    }

    public Optional<UserEntity> getEmployeeById(Long id) {
        return userRepository.findById(id);
    }
}
