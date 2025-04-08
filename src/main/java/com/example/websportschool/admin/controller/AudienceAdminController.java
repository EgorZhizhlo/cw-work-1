package com.example.websportschool.admin.controller;

import com.example.websportschool.entity.AudienceEntity;
import com.example.websportschool.repository.AudienceEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/audiences")
public class AudienceAdminController {

    @Autowired
    private AudienceEntityRepository audienceRepository;

    @GetMapping()
    public String listAudiences(Model model) {
        List<AudienceEntity> audiences = audienceRepository.findAll(Sort.by("id"));
        model.addAttribute("audiences", audiences);
        return "admin/audiences/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("audience", new AudienceEntity());
        return "admin/audiences/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<AudienceEntity> audienceOpt = audienceRepository.findById(id);
        if (audienceOpt.isPresent()) {
            model.addAttribute("audience", audienceOpt.get());
            return "admin/audiences/form";
        }
        return "redirect:/admin/audiences";
    }

    @PostMapping("/create")
    public String createAudience(@ModelAttribute("audience") AudienceEntity audience) {
        audienceRepository.save(audience);
        return "redirect:/admin/audiences";
    }

    @PostMapping("/update")
    public String updateAudience(@ModelAttribute("audience") AudienceEntity audience) {
        Optional<AudienceEntity> audienceOpt = audienceRepository.findById(audience.getId());
        if (audienceOpt.isPresent()) {
            AudienceEntity existingAudience = audienceOpt.get();
            existingAudience.setName(audience.getName());
            audienceRepository.save(existingAudience);
        }
        return "redirect:/admin/audiences";
    }

    @GetMapping("/delete/{id}")
    public String deleteAudience(@PathVariable("id") Long id) {
        audienceRepository.deleteById(id);
        return "redirect:/admin/audiences";
    }
}
