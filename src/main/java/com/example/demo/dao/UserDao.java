package com.example.demo.dao;

import com.example.demo.entity.User;

public interface UserDao {
	User loginUser(String userId);
	int insertUser(String userId, String password);
}
