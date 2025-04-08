package com.example.websportschool.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "news")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id")
    private Long id;

    @Lob
    private byte[] image;

    @Column(length = 255)
    private String name;

    @Column(columnDefinition = "text")
    private String additionalInfo;

    // Добавляем дату публикации
    @Column(name = "publication_date")
    private LocalDateTime publicationDate;

}
