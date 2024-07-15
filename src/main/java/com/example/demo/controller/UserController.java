package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("api/users")
public class UserController {
    @Autowired
    private UserService userService;

    private static final String AUTHORIZATION_HEADER = "Authorization";

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.register(userDTO));
    }


    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody UserDTO userDTO) {
        // Access login credentials from LoginCredentials object
        String mobileNo = userDTO.getMobileNo();
        String password = userDTO.getPassword();
        return ResponseEntity.ok(userService.login(mobileNo, password));
    }

//
//    @GetMapping("/fetch-user-details")
//    public ResponseEntity<UserDTO> fetchUserDetails(@RequestHeader final HttpHeaders httpHeaders) {
//        Optional<List<String>> authorizedTokenOptional = Optional.ofNullable(httpHeaders.get(AUTHORIZATION_HEADER));
//        if (!authorizedTokenOptional.isPresent() || authorizedTokenOptional.get().isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Adjust as needed
//        }
//
//        String token = authorizedTokenOptional.get().get(0);
//        if (!JwtUtil.isValid(token)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//        String mobileNo = JwtUtil.authenticate(token, "mobileNo"); // Assuming "mobileNo" is the claim
//        UserDTO userDTO = userService.fetchUserDetails(mobileNo);
//        return new ResponseEntity<>(userDTO, HttpStatus.OK);
//    }

    @GetMapping("/fetch-user-details")
    public ResponseEntity<User> fetchUserDetails(@RequestHeader final HttpHeaders httpHeaders) {
        Optional<List<String>> authorizedTokenOptional = Optional.ofNullable(httpHeaders.get(AUTHORIZATION_HEADER));
        if (!authorizedTokenOptional.isPresent() || authorizedTokenOptional.get().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Adjust as needed
        }

        String token = authorizedTokenOptional.get().get(0);

        if (!JwtUtil.isValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String mobileNo = JwtUtil.authenticate(token, "mobileNo"); // Assuming "mobileNo" is the claim
        UserDTO userDTO = userService.fetchUserDetails(mobileNo);
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setMobileNo(userDTO.getMobileNo());
        user.setPassword(userDTO.getPassword());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @PutMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestHeader final HttpHeaders httpHeaders, @RequestBody UserDTO userDTO) {

        String token = null;

        if (Objects.nonNull(httpHeaders.get(AUTHORIZATION_HEADER))) { // Check if header exists
            List<String> authorizedToken = httpHeaders.get(AUTHORIZATION_HEADER);
            token = authorizedToken.get(0);
        }

        if (!JwtUtil.isValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String mobileNo = JwtUtil.authenticate(token, "mobileNo"); // Assuming "mobileNo" is the claim

        // Validate user existence based on mobile number extracted from token
        if (!userService.isUserExists(mobileNo)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found. Please register.");
        }

        // Update password using mobile number and new password from request body
        boolean isPasswordUpdated = userService.updatePassword(mobileNo, userDTO.getPassword()); // Assuming secure update implementation

        if (isPasswordUpdated) {
            return ResponseEntity.ok("Password updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update password. Please try again later.");
        }
    }




}
