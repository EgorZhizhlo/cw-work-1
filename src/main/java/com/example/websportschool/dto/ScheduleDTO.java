// Файл: ScheduleDTO.java
package com.example.websportschool.dto;

import java.time.LocalDateTime;

public class ScheduleDTO {
    private Long id;
    private LocalDateTime datetime;
    private String activityName;
    private String audienceName; // может быть null

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public LocalDateTime getDatetime() {
        return datetime;
    }
    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }
    public String getActivityName() {
        return activityName;
    }
    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
    public String getAudienceName() {
        return audienceName;
    }
    public void setAudienceName(String audienceName) {
        this.audienceName = audienceName;
    }
}
