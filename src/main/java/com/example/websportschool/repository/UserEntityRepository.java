package com.example.websportschool.repository;
import org.springframework.stereotype.Repository;

import com.example.websportschool.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    List<UserEntity> findByStatusName(String statusName);

    Long countByStatusName(String employee);
}
