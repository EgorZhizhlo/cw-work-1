package com.example.websportschool.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "activity")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    private Long id;

    @Lob
    private byte[] image;

    @Column(length = 100)
    private String name;

    private String activityType;

    private Integer maxPeople;

    @Column(columnDefinition = "text")
    private String additionalInfo;

    @Column(name = "price_in_double", precision = 10, scale = 2)
    private BigDecimal price;

}
