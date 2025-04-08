package com.example.websportschool.repository;

import com.example.websportschool.entity.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NewsEntityRepository extends JpaRepository<NewsEntity, Long> {
}
