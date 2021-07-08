package com.example.demo.entity;

import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table("employee")
public class EmployeeForm {
	  private String emoployee_code;
	  private String password;
}
