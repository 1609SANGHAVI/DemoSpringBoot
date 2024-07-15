package com.example.demo.service;

import com.example.demo.converter.DemoConverter;
import com.example.demo.dto.DemoDto;
import com.example.demo.dto.HttpResponseDTO;
import com.example.demo.dto.StudentDemo;
import com.example.demo.entity.DemoEntity;
import com.example.demo.repository.DemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class DemoService {
    @Autowired
    private DemoRepository demoRepository;

    @Autowired
    private DemoConverter demoConverter;

    @Autowired
    private RestTemplate restTemplate;

//    public List<StudentDemo> getStudent(){
//        String url="http://localhost:8081/api/students";
//        ResponseEntity<List<StudentDemo>> studentDemoResponseEntity=restTemplate.getForEntity(url,StudentDemo.class);
//        return studentDemoResponseEntity.getBody();
//    }


    public DemoDto createDemo(DemoDto demoDto) {
        DemoEntity demoEntity = demoConverter.dtoToEntity(demoDto);
        demoEntity = demoRepository.save(demoEntity);
        return demoConverter.entityToDto(demoEntity);
    }

    public DemoDto getDemoById(Integer id) {
        DemoEntity demoEntity = demoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demo not found"));
        return demoConverter.entityToDto(demoEntity);
    }

    //    public List<DemoDto> getAllDemos() {
//        return demoRepository.findAll().stream()
//                .map(demoConverter::entityToDto)
//                .collect(Collectors.toList());
//    }
    public Page<DemoDto> getAllDemos(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<DemoEntity> demoEntities = demoRepository.findAll(pageable);
        return demoEntities.map(demoConverter::entityToDto);

    }

    public DemoDto updateDemo(Integer id, DemoDto demoDto) {
        DemoEntity demoEntity = demoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demo not found"));
        demoConverter.updateEntityFromDto(demoDto, demoEntity);
        demoEntity = demoRepository.save(demoEntity);
        return demoConverter.entityToDto(demoEntity);
    }

    public void deleteDemo(Integer id) {

        demoRepository.deleteById(id);
    }
//    public HttpResponseDTO saveStudents(final StudentDemo studentDemo) {
//        HttpResponseDTO httResponseDTO = new HttpResponseDTO();
//        HttpHeaders httpHeaders = new HttpHeaders();
//        String url = "http://localhost:8081/api/students";
//        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(studentDemo, httpHeaders), String.class);
//        httResponseDTO.setResponseCode(responseEntity.getStatusCodeValue());
//        httResponseDTO.setResponseMessage("Student details saved successfully");
//        httResponseDTO.setResponseBody(responseEntity.getBody());
//        return httResponseDTO;
//    }


    public HttpResponseDTO saveStudents(final StudentDemo studentDemo) {
        HttpResponseDTO httpResponseDTO = new HttpResponseDTO();
        HttpHeaders httpHeaders = new HttpHeaders();
        String url = "http://localhost:8081/api/students";
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(studentDemo, httpHeaders), String.class);
        System.out.println(responseEntity.getBody());
//        httpResponseDTO.setResponseCode(200);
//        httResponseDTO.setResponseMessage("student details");
        httpResponseDTO.setResponseBody(responseEntity);
        return httpResponseDTO;
    }

    public HttpResponseDTO getAllStudent(StudentDemo studentDemo) {
        HttpResponseDTO httpResponseDTO = new HttpResponseDTO();
        HttpHeaders httpHeaders = new HttpHeaders();
        String url = "http://localhost:8081/api/students";
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
//        String responseEntity=restTemplate.getForObject(url,String.class);
        System.out.println(responseEntity.getBody());
//        httpResponseDTO.setResponseCode(200);
//        httpResponseDTO.setResponseMessage("student details");
        httpResponseDTO.setResponseBody(responseEntity);
        return httpResponseDTO;

    }

//    public HttResponseDTO deleteStudentById(Long studentId) {
//        String url = "http://localhost:8081/api/student/" + studentId; // Replace with actual student service URL
//        try {
//            restTemplate.delete(url);
//            return new HttResponseDTO(HttpStatus.OK.value(), "Student deleted successfully", null);
//        } catch (RestClientException e) {
//            return new HttResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to delete student", e.getMessage());
//        }
//    }

    public HttpResponseDTO deleteStudent(Long studentId) {
        HttpResponseDTO httpResponseDTO = new HttpResponseDTO();
        String url = "http://localhost:8081/api/students/" + studentId;

        try {
            restTemplate.delete(url);
            httpResponseDTO.setResponseCode(200);
            httpResponseDTO.setResponseMessage("Student deleted successfully");
        } catch (RestClientException e) {
            httpResponseDTO.setResponseCode(500);
            httpResponseDTO.setResponseMessage("Failed to delete student");
            httpResponseDTO.setResponseBody(e.getMessage());
        }
        return httpResponseDTO;
    }

    public HttpResponseDTO updateStudent(Integer studentId, StudentDemo studentDemo) {
        HttpResponseDTO httpResponseDTO = new HttpResponseDTO();
        HttpHeaders httpHeaders = new HttpHeaders();
        String url = "http://localhost:8081/api/students/" + studentId;
        HttpEntity<StudentDemo> requestEntity = new HttpEntity<>(studentDemo, httpHeaders);
        restTemplate.put(url, requestEntity);

        return new HttpResponseDTO(HttpStatus.OK.value(), "Student details updated successful", null);
    }
}
