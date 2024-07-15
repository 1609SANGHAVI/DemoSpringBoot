package com.example.demo.service;

import com.example.demo.converter.UserConverter;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private PasswordEncoder passwordEncoder = passwordEncoder();

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public UserDTO register(UserDTO userDTO) {
        if (userRepository.existsByMobileNo(userDTO.getMobileNo())) {
            throw new RuntimeException("User already registered with this mobile number.");
        }
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User user = UserConverter.convertDTOToEntity(userDTO);
        userRepository.save(user);
//        return UserConverter.convertEntityToDTo(user);
        userDTO.setToken(" ");
        return userDTO;
    }

    public UserDTO login(String mobileNo, String password) {
        Optional<User> optionalUser = userRepository.findByMobileNo(mobileNo);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                String token = JwtUtil.generateToken(user.getMobileNo());
                UserDTO userDTO = UserConverter.convertEntityToDTo(user);
                userDTO.setToken(token);
                return userDTO;


            }
        }
        throw new RuntimeException("Invalid mobile number or password.");
    }

    public UserDTO fetchUserDetails(String mobileNo) {
        Optional<User> optionalUser = userRepository.findByMobileNo(mobileNo);
        if (optionalUser.isPresent()) {
            return UserConverter.convertEntityToDTo(optionalUser.get());
        }
        throw new RuntimeException("User not found.");
    }


    public boolean isUserExists(String mobileNo) {
        // Implement logic to check if user exists using mobile number (e.g., userRepository.findByMobileNo(mobileNo) != null)
        return true; // Replace with actual implementation
    }

    public boolean updatePassword(String mobileNo, String newPassword) {
        Optional<User> optionalUser = userRepository.findByMobileNo(mobileNo);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Implement password hashing logic before updating the user object
            String hashedPassword = passwordEncoder.encode(newPassword); // Assuming a passwordEncoder bean
            user.setPassword(hashedPassword);

            userRepository.save(user);
            return true;
        }

        return false;
    }
}
