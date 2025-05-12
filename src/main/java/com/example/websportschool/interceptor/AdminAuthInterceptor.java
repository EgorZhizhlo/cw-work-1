package com.example.websportschool.interceptor;

import com.example.websportschool.entity.UserEntity;
import com.example.websportschool.service.AccountService;
import com.example.websportschool.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

    @Autowired
    private AccountService accountService;

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler
    ) throws Exception {
        String authToken = null;
        // Извлекаем cookie "authToken"
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("authToken".equals(cookie.getName())) {
                    authToken = cookie.getValue();
                    break;
                }
            }
        }
        // Если токен отсутствует или не прошёл валидацию, перенаправляем на страницу ошибки
        if (authToken == null || !JwtUtil.validateToken(authToken)) {
            response.sendRedirect(request.getContextPath() + "/error/403");
            return false;
        }
        // Извлекаем userId из токена посредством JwtUtil
        Long userId = JwtUtil.getUserIdFromToken(authToken);
        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/error/403");
            return false;
        }
        // Получаем пользователя и проверяем, что его статус "admin"
        UserEntity user = accountService.getUserById(userId);
        if (user == null || !("admin".equalsIgnoreCase(user.getStatusName()))) {
            response.sendRedirect(request.getContextPath() + "/error/403");
            return false;
        }
        return true;
    }
}
