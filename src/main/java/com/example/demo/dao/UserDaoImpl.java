package com.example.demo.dao;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.User;


@Component
@Repository
public class UserDaoImpl implements UserDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate; 

	@Override
	public User loginUser(String userId) {
		User userDto = new User();
		
		try {
			String sql = "SELECT * FROM userinfo WHERE userId=?";
			
			Map<String, Object> map = jdbcTemplate.queryForMap(sql, userId);
			userDto.setUserId((String)map.get("userId"));
			userDto.setPassword((String)map.get("password"));
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return userDto;
	}
	
	@Override
	public int insertUser(String userId, String password) {
		
		String sql = "INSERT INTO userInfo (userId, password) VALUES (?, ?)";
		
		int usdt = jdbcTemplate.update(sql, userId, password);
		
		return usdt;
	}
}
