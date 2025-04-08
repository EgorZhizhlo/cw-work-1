package com.example.websportschool.admin.controller;

import com.example.websportschool.entity.ScheduleEntity;
import com.example.websportschool.repository.ScheduleEntityRepository;
import com.example.websportschool.repository.UserEntityRepository;
import com.example.websportschool.repository.ActivityEntityRepository;
import com.example.websportschool.repository.AudienceEntityRepository;
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
    private UserEntityRepository userRepository;

    @Autowired
    private ActivityEntityRepository activityRepository;

    @Autowired
    private AudienceEntityRepository audienceRepository;

    @GetMapping()
    public String listSchedules(Model model) {
        List<ScheduleEntity> schedules = scheduleRepository.findAll(Sort.by("id"));
        model.addAttribute("schedules", schedules);
        return "admin/schedules/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("schedule", new ScheduleEntity());
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("activities", activityRepository.findAll());
        model.addAttribute("audiences", audienceRepository.findAll());
        return "admin/schedules/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<ScheduleEntity> scheduleOpt = scheduleRepository.findById(id);
        if (scheduleOpt.isPresent()) {
            model.addAttribute("schedule", scheduleOpt.get());
            model.addAttribute("users", userRepository.findAll());
            model.addAttribute("activities", activityRepository.findAll());
            model.addAttribute("audiences", audienceRepository.findAll());
            return "admin/schedules/form";
        }
        return "redirect:/admin/schedules";
    }

    @PostMapping("/create")
    public String createSchedule(@ModelAttribute("schedule") ScheduleEntity schedule) {
        scheduleRepository.save(schedule);
        return "redirect:/admin/schedules";
    }

    @PostMapping("/update")
    public String updateSchedule(@ModelAttribute("schedule") ScheduleEntity schedule) {
        Optional<ScheduleEntity> scheduleOpt = scheduleRepository.findById(schedule.getId());
        if (scheduleOpt.isPresent()) {
            ScheduleEntity existingSchedule = scheduleOpt.get();
            existingSchedule.setUser(schedule.getUser());
            existingSchedule.setActivity(schedule.getActivity());
            existingSchedule.setAudience(schedule.getAudience());
            existingSchedule.setDatetime(schedule.getDatetime());
            scheduleRepository.save(existingSchedule);
        }
        return "redirect:/admin/schedules";
    }

    @GetMapping("/delete/{id}")
    public String deleteSchedule(@PathVariable("id") Long id) {
        scheduleRepository.deleteById(id);
        return "redirect:/admin/schedules";
    }
}
