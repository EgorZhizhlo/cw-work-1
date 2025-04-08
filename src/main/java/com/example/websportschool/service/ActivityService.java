package com.example.websportschool.service;

import com.example.websportschool.entity.ActivityEntity;
import com.example.websportschool.repository.ActivityEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ActivityService {

    private final ActivityEntityRepository activityRepository;

    public ActivityService(ActivityEntityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    // Группировка активности по activityType (заменяем null на "Не указано")
    public Map<String, List<ActivityEntity>> getActivitiesGroupedByType() {
        List<ActivityEntity> activities = activityRepository.findAll();
        return activities.stream()
                .collect(Collectors.groupingBy(a -> a.getActivityType() != null ? a.getActivityType() : "Не указано"));
    }

    // Деление списка активности на чанки по 2 элемента
    public Map<String, List<List<ActivityEntity>>> getActivitiesGroupedByTypeInChunks() {
        Map<String, List<ActivityEntity>> grouped = getActivitiesGroupedByType();
        Map<String, List<List<ActivityEntity>>> result = new HashMap<>();
        for (Map.Entry<String, List<ActivityEntity>> entry : grouped.entrySet()) {
            result.put(entry.getKey(), chunkList(entry.getValue(), 2));
        }
        return result;
    }

    private List<List<ActivityEntity>> chunkList(List<ActivityEntity> list, int chunkSize) {
        List<List<ActivityEntity>> chunks = new ArrayList<>();
        for (int i = 0; i < list.size(); i += chunkSize) {
            chunks.add(list.subList(i, Math.min(i + chunkSize, list.size())));
        }
        return chunks;
    }

    public Optional<ActivityEntity> getActivityById(Long id) {
        return activityRepository.findById(id);
    }
}
