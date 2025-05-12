package com.example.websportschool.service;

import com.example.websportschool.dto.ScheduleDTO;
import com.example.websportschool.entity.ScheduleEntity;
import com.example.websportschool.entity.StudentScheduleEntity;
import com.example.websportschool.entity.UserEntity;
import com.example.websportschool.repository.ScheduleEntityRepository;
import com.example.websportschool.repository.StudentScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    private final ScheduleEntityRepository scheduleRepo;
    private final StudentScheduleRepository studentScheduleRepo;

    @Autowired
    public ScheduleService(ScheduleEntityRepository scheduleRepo,
                           StudentScheduleRepository studentScheduleRepo) {
        this.scheduleRepo = scheduleRepo;
        this.studentScheduleRepo = studentScheduleRepo;
    }

    /** Список DTO занятий, на которые записан студент */
    @Transactional(readOnly = true)
    public List<ScheduleDTO> getScheduleForStudent(UserEntity student) {
        return studentScheduleRepo.findByStudent(student).stream()
                .map(link -> mapToDto(link.getSchedule()))
                .collect(Collectors.toList());
    }

    /** Список DTO занятий, которые ведёт тренер */
    @Transactional(readOnly = true)
    public List<ScheduleDTO> getScheduleForTrainer(UserEntity trainer) {
        return scheduleRepo.findByTrainer(trainer).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /** Детали занятия как DTO */
    @Transactional(readOnly = true)
    public ScheduleDTO getScheduleDtoById(Long id) {
        ScheduleEntity s = getById(id);
        return mapToDto(s);
    }

    /** Получить чистую сущность занятия */
    @Transactional(readOnly = true)
    public ScheduleEntity getById(Long id) {
        return scheduleRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Занятие не найдено: " + id));
    }

    /** Записать студента на занятие */
    @Transactional
    public void signUp(Long scheduleId, UserEntity student) {
        ScheduleEntity sch = getById(scheduleId);
        if (!studentScheduleRepo.existsByStudentAndSchedule(student, sch)) {
            StudentScheduleEntity link = new StudentScheduleEntity();
            link.setSchedule(sch);
            link.setStudent(student);
            studentScheduleRepo.save(link);
        }
    }

    /** Отменить запись студента на занятие */
    @Transactional
    public void cancelSignUp(Long scheduleId, UserEntity student) {
        ScheduleEntity sch = getById(scheduleId);
        studentScheduleRepo.deleteByStudentAndSchedule(student, sch);
    }

    /** Преобразование из сущности в DTO */
    private ScheduleDTO mapToDto(ScheduleEntity s) {
        ScheduleDTO dto = new ScheduleDTO();
        dto.setId(s.getId());
        dto.setDatetime(s.getDatetime());
        dto.setActivityName(s.getActivity().getName());
        dto.setAudienceName(s.getAudience() != null ? s.getAudience().getName() : null);
        dto.setTrainerName(s.getTrainer().getSurname() + " " + s.getTrainer().getName());

        List<String> students = (s.getStudentLinks() == null
                ? Collections.emptyList()
                : s.getStudentLinks().stream()
                .map(ss -> ss.getStudent().getSurname() + " " + ss.getStudent().getName())
                .collect(Collectors.toList()));
        dto.setStudentNames(students);

        return dto;
    }
}
