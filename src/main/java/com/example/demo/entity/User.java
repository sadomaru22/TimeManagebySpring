package com.example.demo.entity;

import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

@Data
@Table("userinfo")
public class User {
	  private String userId;
	  private String password;
}