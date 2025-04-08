package com.example.websportschool.repository;

import com.example.websportschool.entity.ScheduleEntity;
import com.example.websportschool.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
public interface ScheduleEntityRepository extends JpaRepository<ScheduleEntity, Long> {
    List<ScheduleEntity> findByUser(UserEntity user);
}
