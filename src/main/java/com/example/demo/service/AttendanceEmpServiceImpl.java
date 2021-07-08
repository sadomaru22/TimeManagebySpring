package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.AttendanceEmployeeDao;
import com.example.demo.entity.Employee;

@Service
public class AttendanceEmpServiceImpl implements AttendanceEmpService {
	private AttendanceEmployeeDao dao;
	
	@Autowired
	public AttendanceEmpServiceImpl(AttendanceEmployeeDao dao) {
		this.dao = dao;
	}
	
	String id;
	@Override
	public Employee loginEmployee(String id) {
		return dao.loginEmployee(id);
	}
	
	@Override
	public boolean setStartTime(Employee employee) {   //このメソッド名はなんでもいい。おなじの方が安心
		return dao.setStartTime(employee);
	}
	
	@Override
	public boolean setFinishTime(Employee employee) {
		return dao.setFinishTime(employee);
	}
}
