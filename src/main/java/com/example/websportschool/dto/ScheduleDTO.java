package com.example.websportschool.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ScheduleDTO {
    private Long id;
    private LocalDateTime datetime;
    private String activityName;
    private String audienceName;   // может быть null

    // ** Новые поля **
    private String trainerName;
    private List<String> studentNames;

    // Геттеры/сеттеры для старых полей
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getDatetime() { return datetime; }
    public void setDatetime(LocalDateTime datetime) { this.datetime = datetime; }

    public String getActivityName() { return activityName; }
    public void setActivityName(String activityName) { this.activityName = activityName; }

    public String getAudienceName() { return audienceName; }
    public void setAudienceName(String audienceName) { this.audienceName = audienceName; }

    // Геттер/сеттер для trainerName
    public String getTrainerName() { return trainerName; }
    public void setTrainerName(String trainerName) { this.trainerName = trainerName; }

    // Геттер/сеттер для studentNames
    public List<String> getStudentNames() { return studentNames; }
    public void setStudentNames(List<String> studentNames) { this.studentNames = studentNames; }
}
