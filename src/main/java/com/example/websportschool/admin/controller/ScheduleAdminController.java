package com.example.websportschool.admin.controller;

import com.example.websportschool.entity.ScheduleEntity;
import com.example.websportschool.entity.UserEntity;
import com.example.websportschool.repository.ActivityEntityRepository;
import com.example.websportschool.repository.AudienceEntityRepository;
import com.example.websportschool.repository.ScheduleEntityRepository;
import com.example.websportschool.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/schedules")
public class ScheduleAdminController {

    @Autowired
    private ScheduleEntityRepository scheduleRepository;

    @Autowired
    private UserEntityRepository trainerRepository;  // репозиторий пользователей

    @Autowired
    private ActivityEntityRepository activityRepository;

    @Autowired
    private AudienceEntityRepository audienceRepository;

    // Показ списка расписаний
    @GetMapping
    public String listSchedules(Model model) {
        List<ScheduleEntity> schedules = scheduleRepository.findAll(Sort.by("id"));
        model.addAttribute("schedules", schedules);
        return "admin/schedules/list";
    }

    // Форма создания нового занятия
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("schedule", new ScheduleEntity());
        // тренеры — только сотрудники с ролью EMPLOYEE
        List<UserEntity> trainers = trainerRepository.findByStatusName("EMPLOYEE");
        model.addAttribute("trainers", trainers);
        model.addAttribute("activities", activityRepository.findAll());
        model.addAttribute("audiences", audienceRepository.findAll());
        return "admin/schedules/form";
    }

    // Форма редактирования занятия
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<ScheduleEntity> scheduleOpt = scheduleRepository.findById(id);
        if (scheduleOpt.isPresent()) {
            model.addAttribute("schedule", scheduleOpt.get());
            List<UserEntity> trainers = trainerRepository.findByStatusName("EMPLOYEE");
            model.addAttribute("trainers", trainers);
            model.addAttribute("activities", activityRepository.findAll());
            model.addAttribute("audiences", audienceRepository.findAll());
            return "admin/schedules/form";
        }
        return "redirect:/admin/schedules";
    }

    // Создание нового занятия
    @PostMapping("/create")
    public String createSchedule(@ModelAttribute("schedule") ScheduleEntity schedule) {
        scheduleRepository.save(schedule);
        return "redirect:/admin/schedules";
    }

    // Обновление существующего занятия
    @PostMapping("/update")
    public String updateSchedule(@ModelAttribute("schedule") ScheduleEntity schedule) {
        Optional<ScheduleEntity> scheduleOpt = scheduleRepository.findById(schedule.getId());
        if (scheduleOpt.isPresent()) {
            ScheduleEntity existing = scheduleOpt.get();
            existing.setTrainer(schedule.getTrainer());
            existing.setActivity(schedule.getActivity());
            existing.setAudience(schedule.getAudience());
            existing.setDatetime(schedule.getDatetime());
            scheduleRepository.save(existing);
        }
        return "redirect:/admin/schedules";
    }

    // Удаление занятия
    @GetMapping("/delete/{id}")
    public String deleteSchedule(@PathVariable("id") Long id) {
        scheduleRepository.deleteById(id);
        return "redirect:/admin/schedules";
    }
}
