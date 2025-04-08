package com.example.websportschool.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "audience")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AudienceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "audience_id")
    private Long id;

    @Column(length = 155)
    private String name;
}
