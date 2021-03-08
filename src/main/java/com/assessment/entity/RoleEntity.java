package com.assessment.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.assessment.dto.RoleDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "role")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(name = "role_name", nullable = false, unique = true)
    private String name;
	
	@Column(name = "is_active", nullable = false)
	private boolean isActive = true;	
 
	public RoleDTO toRoleDTO() {
		return new RoleDTO(id, name, isActive);
	}

}