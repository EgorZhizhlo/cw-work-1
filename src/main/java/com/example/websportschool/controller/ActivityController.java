package com.example.websportschool.controller;

import com.example.websportschool.entity.ActivityEntity;
import com.example.websportschool.entity.UserEntity;
import com.example.websportschool.service.ActivityService;
import com.example.websportschool.service.AccountService;
import com.example.websportschool.service.ScheduleService;
import com.example.websportschool.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class ActivityController {

    private final ActivityService activityService;
    private final AccountService accountService;
    private final ScheduleService scheduleService;

    @Autowired
    public ActivityController(ActivityService activityService,
                              AccountService accountService,
                              ScheduleService scheduleService) {
        this.activityService = activityService;
        this.accountService = accountService;
        this.scheduleService = scheduleService;
    }

    @GetMapping("/activities")
    public String showActivities(@CookieValue(value = "authToken", required = false) String authToken,
                                 Model model) {
        boolean isAuthenticated = authToken != null && JwtUtil.validateToken(authToken);
        model.addAttribute("isAuthenticated", isAuthenticated);

        Map<String, List<List<ActivityEntity>>> groupedActivitiesChunks =
                activityService.getActivitiesGroupedByTypeInChunks();
        model.addAttribute("groupedActivitiesChunks", groupedActivitiesChunks);
        return "activities";
    }

    @GetMapping("/activities/{id}")
    public String showActivityDetail(@CookieValue(value = "authToken", required = false) String authToken,
                                     @PathVariable("id") Long id,
                                     Model model) {
        boolean isAuthenticated = authToken != null && JwtUtil.validateToken(authToken);
        model.addAttribute("isAuthenticated", isAuthenticated);

        Optional<ActivityEntity> activityOpt = activityService.getActivityById(id);
        if (activityOpt.isEmpty()) {
            return "redirect:/activities";
        }
        ActivityEntity activity = activityOpt.get();
        model.addAttribute("activity", activity);

        // если пользователь авторизован — проверяем, записан ли он уже
        if (isAuthenticated) {
            UserEntity user = accountService.getUserById(
                    JwtUtil.getUserIdFromToken(authToken)
            );
            // тут мы считаем, что activity.id совпадает с schedule.id
            // (чтобы форма «signup» работала по тому же id)
            boolean isSignedUp = scheduleService
                    .getScheduleForStudent(user)
                    .stream()
                    .anyMatch(s -> s.getId().equals(id));
            model.addAttribute("isSignedUp", isSignedUp);
        }

        return "activityDetail";
    }

    /** Обработка записи на занятие из страницы услуги */
    @PostMapping("/activities/{id}/signup")
    public String signupFromActivity(@PathVariable("id") Long id,
                                     @CookieValue("authToken") String authToken) {
        UserEntity user = accountService.getUserById(
                JwtUtil.getUserIdFromToken(authToken)
        );
        scheduleService.signUp(id, user);
        return "redirect:/activities/" + id;
    }

    /** Обработка отмены записи из страницы услуги */
    @PostMapping("/activities/{id}/cancel")
    public String cancelFromActivity(@PathVariable("id") Long id,
                                     @CookieValue("authToken") String authToken) {
        UserEntity user = accountService.getUserById(
                JwtUtil.getUserIdFromToken(authToken)
        );
        scheduleService.cancelSignUp(id, user);
        return "redirect:/activities/" + id;
    }

    @GetMapping("/activity/image/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<byte[]> getActivityImage(@PathVariable("id") Long id) {
        Optional<ActivityEntity> activityOpt = activityService.getActivityById(id);
        if (activityOpt.isPresent() && activityOpt.get().getImage() != null) {
            byte[] image = activityOpt.get().getImage();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(image, headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
