package com.example.myshop.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponse {

    private Integer _id;
    private String username;
    private String email;
    private Integer isVerify;
    private String type;
    private  String role;


}
