package com.example.demo.controller;

import com.example.demo.dto.DemoDto;
import com.example.demo.dto.HttpResponseDTO;
import com.example.demo.dto.StudentDemo;
import com.example.demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import java.util.List;

@RestController
@RequestMapping ("/api/demo")
public class DemoController {

    @Autowired
    private DemoService demoService;

    @PostMapping("/save")
    public ResponseEntity<HttpResponseDTO> getStudent(@RequestBody StudentDemo studentDemo) {
        return ResponseEntity.ok(demoService.saveStudents(studentDemo));

    }


    @GetMapping("/fetch")
    public ResponseEntity<HttpResponseDTO> getAllStudent() {
        StudentDemo studentDemo = new StudentDemo();
        return ResponseEntity.ok(demoService.getAllStudent(studentDemo));
    }


    @PutMapping("/update/{studentId}")
    public ResponseEntity<HttpResponseDTO> updateStudentDetails(@PathVariable Integer studentId, @RequestBody StudentDemo studentDemo) {
        try {
            HttpResponseDTO response = demoService.updateStudent(studentId,studentDemo);
            return ResponseEntity.ok(response);
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new HttpResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update student details", e.getMessage()));
        }

    }



    @PostMapping("/create")
    public ResponseEntity<DemoDto> createDemo(@RequestBody DemoDto demoDto) {
        return ResponseEntity.ok(demoService.createDemo(demoDto));
    }


    @GetMapping("/{id}")
    public ResponseEntity<DemoDto> getDemoById(@PathVariable Integer id) {
        return ResponseEntity.ok(demoService.getDemoById(id));
    }

    @GetMapping("/get")
    public ResponseEntity<List<DemoDto>> getAllDemos(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "5") int pageSize) {

        Page<DemoDto> demoDtos = demoService.getAllDemos(pageNo, pageSize);
        List<DemoDto> demoDtoList = demoDtos.getContent(); // Extract the content from the Page
        return ResponseEntity.ok(demoDtoList);
    }


    @PutMapping("/{id}")
    public ResponseEntity<DemoDto> updateDemo(@PathVariable Integer id, @RequestBody DemoDto demoDto) {
        return ResponseEntity.ok(demoService.updateDemo(id, demoDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDemo(@PathVariable Integer id) {
        demoService.deleteDemo(id);
        return ResponseEntity.noContent().build();
    }




    @DeleteMapping("/students/{studentId}")
    public ResponseEntity<HttpResponseDTO> deleteStudent(@PathVariable Long studentId) {
        HttpResponseDTO responseDTO = demoService.deleteStudent(studentId);
        return ResponseEntity
                .status(responseDTO.getResponseCode())
                .body(responseDTO);
    }

}
