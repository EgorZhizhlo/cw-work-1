package com.example.websportschool.controller;

import com.example.websportschool.entity.ActivityEntity;
import com.example.websportschool.service.ActivityService;
import com.example.websportschool.util.JwtUtil;
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

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping("/activities")
    public String showActivities(@CookieValue(value = "authToken", required = false) String authToken, Model model) {
        boolean isAuthenticated = authToken != null && JwtUtil.validateToken(authToken);
        model.addAttribute("isAuthenticated", isAuthenticated);

        Map<String, List<List<ActivityEntity>>> groupedActivitiesChunks = activityService.getActivitiesGroupedByTypeInChunks();
        model.addAttribute("groupedActivitiesChunks", groupedActivitiesChunks);
        return "activities";
    }

    @GetMapping("/activities/{id}")
    public String showActivityDetail(@CookieValue(value = "authToken", required = false) String authToken,
                                     @PathVariable("id") Long id, Model model) {
        boolean isAuthenticated = authToken != null && JwtUtil.validateToken(authToken);
        model.addAttribute("isAuthenticated", isAuthenticated);

        Optional<ActivityEntity> activityOpt = activityService.getActivityById(id);
        if (activityOpt.isPresent()) {
            model.addAttribute("activity", activityOpt.get());
            return "activityDetail";
        }
        return "redirect:/activities";
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
