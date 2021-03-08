package com.assessment.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String userName;
    private String firstName;
    private String lastName;    
    private String email;
    private boolean isActive;
    private int wrongPasswordEntries;
    private boolean isLocked;
    private List<String> roles;
    
}