package com.example.demo.converter;

import com.example.demo.dto.DemoDto;
import com.example.demo.entity.DemoEntity;
import org.springframework.stereotype.Component;

@Component
public class DemoConverter {
    public DemoDto entityToDto(DemoEntity demoEntity) {
        return DemoDto.builder()
                .id(demoEntity.getId())
                .mobileType(demoEntity.getMobileType())
                .mobileName(demoEntity.getMobileName())
                .price(demoEntity.getPrice())
                .color(demoEntity.getColor())
                .build();
    }

    public DemoEntity dtoToEntity(DemoDto demoDto) {
        return DemoEntity.builder()
                .id(demoDto.getId())
                .mobileType(demoDto.getMobileType())
                .mobileName(demoDto.getMobileName())
                .price(demoDto.getPrice())
                .color(demoDto.getColor())
                .build();
    }

    public void updateEntityFromDto(DemoDto demoDto, DemoEntity demoEntity) {
        demoEntity.setMobileType(demoDto.getMobileType());
        demoEntity.setMobileName(demoDto.getMobileName());
        demoEntity.setPrice(demoDto.getPrice());
        demoEntity.setColor(demoDto.getColor());
    }
}
