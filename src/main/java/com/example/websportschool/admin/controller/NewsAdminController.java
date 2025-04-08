package com.example.websportschool.admin.controller;

import com.example.websportschool.entity.NewsEntity;
import com.example.websportschool.repository.NewsEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin/news")
public class NewsAdminController {

    @Autowired
    private NewsEntityRepository newsRepository;

    @GetMapping()
    public String listNews(Model model) {
        List<NewsEntity> newsList = newsRepository.findAll(Sort.by("id"));
        Map<Long, String> newsImages = new HashMap<>();
        for (NewsEntity news : newsList) {
            if (news.getImage() != null) {
                String base64Image = Base64.getEncoder().encodeToString(news.getImage());
                newsImages.put(news.getId(), base64Image);
            }
        }
        model.addAttribute("newsList", newsList);
        model.addAttribute("newsImages", newsImages);
        return "admin/news/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("news", new NewsEntity());
        return "admin/news/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<NewsEntity> newsOpt = newsRepository.findById(id);
        if (newsOpt.isPresent()) {
            model.addAttribute("news", newsOpt.get());
            return "admin/news/form";
        }
        return "redirect:/admin/news";
    }

    @PostMapping("/create")
    public String createNews(@ModelAttribute("news") NewsEntity news,
                             @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        if (!imageFile.isEmpty()) {
            news.setImage(imageFile.getBytes());
        }
        news.setPublicationDate(LocalDateTime.now());
        newsRepository.save(news);
        return "redirect:/admin/news";
    }

    @PostMapping("/update")
    public String updateNews(@ModelAttribute("news") NewsEntity news,
                             @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        Optional<NewsEntity> newsOpt = newsRepository.findById(news.getId());
        if (newsOpt.isPresent()) {
            NewsEntity existingNews = newsOpt.get();
            existingNews.setName(news.getName());
            existingNews.setAdditionalInfo(news.getAdditionalInfo());
            if (!imageFile.isEmpty()) {
                existingNews.setImage(imageFile.getBytes());
            }
            existingNews.setPublicationDate(news.getPublicationDate());
            newsRepository.save(existingNews);
        }
        return "redirect:/admin/news";
    }

    @GetMapping("/delete/{id}")
    public String deleteNews(@PathVariable("id") Long id) {
        newsRepository.deleteById(id);
        return "redirect:/admin/news";
    }
}
