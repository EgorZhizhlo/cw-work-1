package com.example.websportschool.repository;

import com.example.websportschool.entity.ScheduleEntity;
import com.example.websportschool.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ScheduleEntityRepository extends JpaRepository<ScheduleEntity, Long> {

    // По тренеру
    List<ScheduleEntity> findByTrainer(UserEntity trainer);
}
