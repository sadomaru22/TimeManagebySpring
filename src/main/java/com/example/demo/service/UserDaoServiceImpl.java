package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.UserDao;
import com.example.demo.entity.User;

@Service
public class UserDaoServiceImpl implements UserDaoService {
	private UserDao dao;
	
	@Autowired
	public UserDaoServiceImpl(UserDao dao) {
		this.dao = dao;
	}
	
	@Override
	public User loginUser(String userId) {
		return dao.loginUser(userId);
	}
	
	@Override
	public int insertUser(String userId, String password) {
		return dao.insertUser(userId, password);
	}
}
