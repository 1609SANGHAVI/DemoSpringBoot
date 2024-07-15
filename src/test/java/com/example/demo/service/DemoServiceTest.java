package com.example.demo.service;

import com.example.demo.converter.DemoConverter;
import com.example.demo.dto.DemoDto;
import com.example.demo.entity.DemoEntity;
import com.example.demo.repository.DemoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DemoServiceTest {
    @Mock
    private DemoRepository demoRepository;

    @Mock
    private DemoConverter demoConverter;

    @InjectMocks
    private DemoService demoService;

    private DemoDto demoDto;
    private DemoEntity demoEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        demoDto = DemoDto.builder()
                .id(1)
                .mobileType(DemoEntity.MobileType.SMARTPHONE)
                .mobileName("Nokia")
                .price(500)
                .color("Black")
                .build();

        demoEntity = DemoEntity.builder()
                .id(1)
                .mobileType(DemoEntity.MobileType.SMARTPHONE)
                .mobileName("Nokia")
                .price(500)
                .color("Black")
                .build();
    }

    @Test
    void createDemo() {
        when(demoConverter.dtoToEntity(any(DemoDto.class))).thenReturn(demoEntity);
        when(demoRepository.save(any(DemoEntity.class))).thenReturn(demoEntity);
        when(demoConverter.entityToDto(any(DemoEntity.class))).thenReturn(demoDto);

        DemoDto createdDemo = demoService.createDemo(demoDto);

        assertEquals(demoDto.getId(), createdDemo.getId());
        assertEquals(demoDto.getMobileType(), createdDemo.getMobileType());
        assertEquals(demoDto.getMobileName(), createdDemo.getMobileName());
        assertEquals(demoDto.getPrice(), createdDemo.getPrice());
        assertEquals(demoDto.getColor(), createdDemo.getColor());
        verify(demoRepository, times(1)).save(any(DemoEntity.class));
    }

    @Test
    void getDemoById() {
        when(demoRepository.findById(anyInt())).thenReturn(Optional.of(demoEntity));
        when(demoConverter.entityToDto(any(DemoEntity.class))).thenReturn(demoDto);

        DemoDto foundDemo = demoService.getDemoById(1);

        assertEquals(demoDto.getId(), foundDemo.getId());
        assertEquals(demoDto.getMobileType(), foundDemo.getMobileType());
        assertEquals(demoDto.getMobileName(), foundDemo.getMobileName());
        assertEquals(demoDto.getPrice(), foundDemo.getPrice());
        assertEquals(demoDto.getColor(), foundDemo.getColor());
        verify(demoRepository, times(1)).findById(anyInt());
    }

    @Test
    void getDemoById_NotFound() {
        when(demoRepository.findById(anyInt())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> demoService.getDemoById(1));

        assertEquals("Demo not found", exception.getMessage());
        verify(demoRepository, times(1)).findById(anyInt());
    }

    @Test
    void getAllDemos() {
        Page<DemoEntity> demoEntities = new PageImpl<>(Collections.singletonList(demoEntity));
        when(demoRepository.findAll(any(Pageable.class))).thenReturn(demoEntities);
        when(demoConverter.entityToDto(any(DemoEntity.class))).thenReturn(demoDto);

        Page<DemoDto> result = demoService.getAllDemos(0, 5);

        assertEquals(1, result.getTotalElements());
        assertEquals(demoDto.getId(), result.getContent().get(0).getId());
        assertEquals(demoDto.getMobileType(), result.getContent().get(0).getMobileType());
        assertEquals(demoDto.getMobileName(), result.getContent().get(0).getMobileName());
        assertEquals(demoDto.getPrice(), result.getContent().get(0).getPrice());
        assertEquals(demoDto.getColor(), result.getContent().get(0).getColor());
        verify(demoRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void updateDemo() {
        when(demoRepository.findById(anyInt())).thenReturn(Optional.of(demoEntity));
        when(demoRepository.save(any(DemoEntity.class))).thenReturn(demoEntity);
        when(demoConverter.entityToDto(any(DemoEntity.class))).thenReturn(demoDto);
        doNothing().when(demoConverter).updateEntityFromDto(any(DemoDto.class), any(DemoEntity.class));

        DemoDto updatedDemo = demoService.updateDemo(1, demoDto);

        assertEquals(demoDto.getId(), updatedDemo.getId());
        assertEquals(demoDto.getMobileType(), updatedDemo.getMobileType());
        assertEquals(demoDto.getMobileName(), updatedDemo.getMobileName());
        assertEquals(demoDto.getPrice(), updatedDemo.getPrice());
        assertEquals(demoDto.getColor(), updatedDemo.getColor());
        verify(demoRepository, times(1)).findById(anyInt());
        verify(demoRepository, times(1)).save(any(DemoEntity.class));
    }

    @Test
    void updateDemo_NotFound() {
        when(demoRepository.findById(anyInt())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> demoService.updateDemo(1, demoDto));

        assertEquals("Demo not found", exception.getMessage());
        verify(demoRepository, times(1)).findById(anyInt());
    }

    @Test
    void deleteDemo() {
        doNothing().when(demoRepository).deleteById(anyInt());

        demoService.deleteDemo(1);

        verify(demoRepository, times(1)).deleteById(anyInt());
    }
}
