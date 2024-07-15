package com.example.demo.controller;

import com.example.demo.dto.DemoDto;
import com.example.demo.entity.DemoEntity;
import com.example.demo.service.DemoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


@ExtendWith(MockitoExtension.class)
public class DemoControllerTest {
    private MockMvc mockMvc;

    @Mock
    private DemoService demoService;

    @InjectMocks
    private DemoController demoController;

    private DemoDto demoDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(demoController).build();

        demoDto = DemoDto.builder()
                .id(1)
                .mobileType(DemoEntity.MobileType.SMARTPHONE)
                .mobileName("Nokia")
                .price(500)
                .color("Black")
                .build();
    }
    @Test
    void createDemo() throws Exception {
        when(demoService.createDemo(any(DemoDto.class))).thenReturn(demoDto);

        // Logging the demoDto values before performing the request
        System.out.println("DemoDto values before createDemo request:");
        System.out.println("ID: " + demoDto.getId());
        System.out.println("Mobile Type: " + demoDto.getMobileType());
        System.out.println("Mobile Name: " + demoDto.getMobileName());
        System.out.println("Price: " + demoDto.getPrice());
        System.out.println("Color: " + demoDto.getColor());

        MvcResult mvcResult = mockMvc.perform(post("/api/demo/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(demoDto)))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());
        verify(demoService, times(1)).createDemo(any(DemoDto.class));
    }


    @Test
    void getDemoById() throws Exception {
        when(demoService.getDemoById(1)).thenReturn(demoDto);

        MvcResult mvcResult = mockMvc.perform(get("/api/demo/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());
        DemoDto result = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), DemoDto.class);
        assertEquals(demoDto.getId(), result.getId());
        assertEquals(demoDto.getMobileType(), result.getMobileType());
        assertEquals(demoDto.getMobileName(), result.getMobileName());
        assertEquals(demoDto.getPrice(), result.getPrice());
        assertEquals(demoDto.getColor(), result.getColor());


        verify(demoService, times(1)).getDemoById(1);
    }
    @Test
    void updateDemo() throws Exception {
        when(demoService.updateDemo(eq(1), any(DemoDto.class))).thenReturn(demoDto);

        // Logging the demoDto values before performing the request
        System.out.println("DemoDto values before updateDemo request:");
        System.out.println("ID: " + demoDto.getId());
        System.out.println("Mobile Type: " + demoDto.getMobileType());
        System.out.println("Mobile Name: " + demoDto.getMobileName());
        System.out.println("Price: " + demoDto.getPrice());
        System.out.println("Color: " + demoDto.getColor());

        MvcResult mvcResult = mockMvc.perform(put("/api/demo/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(demoDto)))
                .andReturn();

        assertEquals(200, mvcResult.getResponse().getStatus());
        verify(demoService, times(1)).updateDemo(eq(1), any(DemoDto.class));
    }


    @Test
    void deleteDemo() throws Exception {
        doNothing().when(demoService).deleteDemo(1);

        MvcResult mvcResult = mockMvc.perform(delete("/api/demo/{id}", 1))
                .andReturn();

        assertEquals(204, mvcResult.getResponse().getStatus());
        verify(demoService, times(1)).deleteDemo(1);
    }
}


