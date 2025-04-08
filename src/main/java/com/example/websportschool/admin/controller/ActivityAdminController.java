package com.example.websportschool.admin.controller;

import com.example.websportschool.entity.ActivityEntity;
import com.example.websportschool.repository.ActivityEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin/activities")
public class ActivityAdminController {

    @Autowired
    private ActivityEntityRepository activityRepository;

    @GetMapping()
    public String listActivities(Model model) {
        List<ActivityEntity> activities = activityRepository.findAll(Sort.by("id"));
        Map<Long, String> activityImages = new HashMap<>();
        for (ActivityEntity activity : activities) {
            if (activity.getImage() != null) {
                String base64Image = Base64.getEncoder().encodeToString(activity.getImage());
                activityImages.put(activity.getId(), base64Image);
            }
        }
        model.addAttribute("activities", activities);
        model.addAttribute("activityImages", activityImages);
        return "admin/activities/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("activity", new ActivityEntity());
        return "admin/activities/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<ActivityEntity> activityOpt = activityRepository.findById(id);
        if (activityOpt.isPresent()) {
            model.addAttribute("activity", activityOpt.get());
            return "admin/activities/form";
        }
        return "redirect:/admin/activities";
    }

    @PostMapping("/create")
    public String createActivity(@ModelAttribute("activity") ActivityEntity activity,
                                 @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        if (!imageFile.isEmpty()) {
            activity.setImage(imageFile.getBytes());
        }
        activityRepository.save(activity);
        return "redirect:/admin/activities";
    }

    @PostMapping("/update")
    public String updateActivity(@ModelAttribute("activity") ActivityEntity activity,
                                 @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        Optional<ActivityEntity> activityOpt = activityRepository.findById(activity.getId());
        if (activityOpt.isPresent()) {
            ActivityEntity existingActivity = activityOpt.get();
            existingActivity.setName(activity.getName());
            existingActivity.setActivityType(activity.getActivityType());
            existingActivity.setMaxPeople(activity.getMaxPeople());
            existingActivity.setAdditionalInfo(activity.getAdditionalInfo());
            existingActivity.setPrice(activity.getPrice());
            if (!imageFile.isEmpty()) {
                existingActivity.setImage(imageFile.getBytes());
            }
            activityRepository.save(existingActivity);
        }
        return "redirect:/admin/activities";
    }

    @GetMapping("/delete/{id}")
    public String deleteActivity(@PathVariable("id") Long id) {
        activityRepository.deleteById(id);
        return "redirect:/admin/activities";
    }
}
