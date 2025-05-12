package com.example.websportschool.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    // Хранение изображения в виде массива байтов
    @Lob
    private byte[] image;

    @Column(length = 100)
    private String surname;

    @Column(length = 100)
    private String name;

    @Column(length = 100)
    private String patronymic;

    @Column(length = 150, unique = true)
    private String email;

    @Column(length = 18, unique = true)
    private String phoneNumber;

    // Зашифрованный пароль
    @Column(nullable = false, length = 255)
    private String password;

    @Column(length = 50, nullable = false)
    private String statusName;

    @Column(length = 100)
    private String specialization;

    private Integer workExperience;

    @Column(columnDefinition = "text")
    private String additionalInfo;

    // Занятия, которые ведёт этот пользователь (если он тренер)
    @OneToMany(mappedBy = "trainer")
    private List<ScheduleEntity> schedulesTaught;

    // Занятия, на которые записан этот пользователь (как студент)
    @OneToMany(mappedBy = "student")
    private List<StudentScheduleEntity> schedulesAttended;
}
