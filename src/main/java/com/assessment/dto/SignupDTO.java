package com.assessment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupDTO {

    private String userName;
    private String firstName;
    private String lastName;    
    private String email;
    private String password;
    
}
