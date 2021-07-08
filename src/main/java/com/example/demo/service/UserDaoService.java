package com.example.demo.service;

import com.example.demo.entity.User;

public interface UserDaoService {
	User loginUser(String userId);
	int insertUser(String userId, String password);
}
