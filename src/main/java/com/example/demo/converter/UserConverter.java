package com.example.demo.converter;


import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    public static User convertDTOToEntity(UserDTO userDTO){
        User user=new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setMobileNo(userDTO.getMobileNo());
        user.setPassword(userDTO.getPassword());
        return user;
    }
    public static  UserDTO convertEntityToDTo(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .mobileNo(user.getMobileNo())
                .password(user.getPassword())

                .build();
    }
}
