package com.example.demo.dto;

import com.example.demo.entity.DemoEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DemoDto {
    @JsonProperty("Id")
    private Integer id;
    private DemoEntity.MobileType mobileType;
    @JsonProperty("mobileName")
    private String mobileName;
    @JsonProperty("price")
    private Integer price;
    @JsonProperty("color")
    private String color;
}
