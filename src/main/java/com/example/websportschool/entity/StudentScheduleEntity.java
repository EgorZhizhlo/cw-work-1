package com.example.websportschool.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student_schedule")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_schedule_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private UserEntity student;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private ScheduleEntity schedule;
}
