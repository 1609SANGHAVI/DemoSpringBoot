package com.example.demo.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserDTO {
    private int id;
    private String name;
    private String mobileNo;
    private String password;
    private String token="";
}
