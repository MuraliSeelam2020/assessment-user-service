package com.assessment.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.assessment.dto.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @Column(name = "first_name", nullable = false, updatable = false)
    private String firstName;
    
    @Column(name = "last_name", nullable = false, updatable = false)
    private String lastName;
    
    private String email;
    private String password;

	@Column(name = "is_active", nullable = false)
	private boolean isActive = true;
    
    @Column(name = "wrong_password_entries", nullable = false)
    private int wrongPasswordEntries;

	@Column(name = "is_locked", nullable = false)
	private boolean isLocked = false;
	
	public UserDTO toUserDTO() {
		return new UserDTO(id, userName, firstName, lastName, email, isActive, wrongPasswordEntries, isLocked, null);
	}
    
}