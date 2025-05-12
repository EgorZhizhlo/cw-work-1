package com.example.websportschool.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "schedule")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    // Тренер, ведущий занятие
    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
    private UserEntity trainer;

    @ManyToOne
    @JoinColumn(name = "activity_id", nullable = false)
    private ActivityEntity activity;

    @ManyToOne
    @JoinColumn(name = "audience_id")
    private AudienceEntity audience;

    private LocalDateTime datetime;

    // Список записавшихся студентов (двунаправленный, опционально)
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentScheduleEntity> studentLinks;
}
