package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="mobile")
public class DemoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("Id")
    private Integer id;
    @Column
    @Enumerated(EnumType.STRING)
    private MobileType mobileType;

    @Column
    @JsonProperty("mobileName")
    private String mobileName;

    @Column
    @JsonProperty("price")
    private Integer price;

    @Column
    @JsonIgnore
    private String color;


    public enum MobileType {
        SMARTPHONE,
        TABLET,
        FEATURE_PHONE
    }
}
