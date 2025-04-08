package com.example.websportschool.controller;

import com.example.websportschool.entity.NewsEntity;
import com.example.websportschool.repository.NewsEntityRepository;
import com.example.websportschool.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class HomeController {
    @Autowired
    private NewsEntityRepository newsRepository;

    @GetMapping("/")
    public String homePage(@CookieValue(value = "authToken", required = false) String authToken, Model model) {
        boolean isAuthenticated = authToken != null && JwtUtil.validateToken(authToken);
        model.addAttribute("isAuthenticated", isAuthenticated);

        List<NewsEntity> allNews = newsRepository.findAll(Sort.by(Sort.Direction.DESC, "publicationDate"));
        List<NewsEntity> sliderNews = allNews.stream().limit(10).collect(Collectors.toList());
        model.addAttribute("sliderNews", sliderNews);

        return "index";
    }

    @GetMapping("/about")
    public String aboutPage(@CookieValue(value = "authToken", required = false) String authToken, Model model) {
        boolean isAuthenticated = authToken != null && JwtUtil.validateToken(authToken);
        model.addAttribute("isAuthenticated", isAuthenticated);

        return "about";
    }

    @GetMapping("/news/{id}")
    public String newsDetail(@PathVariable("id") Long id,
                             Model model,
                             @CookieValue(value = "authToken", required = false) String authToken) {
        boolean isAuthenticated = authToken != null && JwtUtil.validateToken(authToken);
        model.addAttribute("isAuthenticated", isAuthenticated);

        Optional<NewsEntity> newsOpt = newsRepository.findById(id);
        if (newsOpt.isPresent()) {
            model.addAttribute("news", newsOpt.get());
            return "newsDetail";
        }
        return "redirect:/";
    }

    @GetMapping("/news/image/{id}")
    public ResponseEntity<byte[]> getNewsImage(@PathVariable("id") Long id) {
        Optional<NewsEntity> newsOpt = newsRepository.findById(id);
        if (newsOpt.isPresent() && newsOpt.get().getImage() != null) {
            byte[] image = newsOpt.get().getImage();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(image, headers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
