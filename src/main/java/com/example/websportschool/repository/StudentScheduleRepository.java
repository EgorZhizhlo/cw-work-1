package com.example.websportschool.repository;

import com.example.websportschool.entity.ScheduleEntity;
import com.example.websportschool.entity.StudentScheduleEntity;
import com.example.websportschool.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentScheduleRepository extends JpaRepository<StudentScheduleEntity, Long> {

    // Все link-записи для данного студента
    List<StudentScheduleEntity> findByStudent(UserEntity student);

    // Все link-записи для данного расписания
    List<StudentScheduleEntity> findByScheduleId(Long scheduleId);

    // Проверить, записан ли студент на занятие
    boolean existsByStudentAndSchedule(UserEntity student, ScheduleEntity schedule);

    // Удалить запись студента на занятие
    void deleteByStudentAndSchedule(UserEntity student, ScheduleEntity schedule);
}
