// Файл: ScheduleService.java
package com.example.websportschool.service;

import com.example.websportschool.dto.ScheduleDTO;
import com.example.websportschool.entity.ScheduleEntity;
import com.example.websportschool.entity.UserEntity;
import com.example.websportschool.repository.ScheduleEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final ScheduleEntityRepository scheduleRepository;

    @Autowired
    public ScheduleService(ScheduleEntityRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }
    @Transactional(readOnly = true)

    public List<ScheduleDTO> getScheduleForUser(UserEntity user) {
        List<ScheduleEntity> schedules = scheduleRepository.findByUser(user);
        return schedules.stream().map(schedule -> {
            ScheduleDTO dto = new ScheduleDTO();
            dto.setId(schedule.getId());
            dto.setDatetime(schedule.getDatetime());
            dto.setActivityName(schedule.getActivity().getName());
            if (schedule.getAudience() != null) {
                dto.setAudienceName(schedule.getAudience().getName());
            }
            return dto;
        }).collect(Collectors.toList());
    }
}
