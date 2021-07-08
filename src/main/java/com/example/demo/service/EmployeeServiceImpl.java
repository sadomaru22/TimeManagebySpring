package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.EmployeeDao;
import com.example.demo.entity.Employee;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	private EmployeeDao dao;
	
	@Autowired
	public EmployeeServiceImpl(EmployeeDao dao) {
		this.dao = dao;
	}
	
	@Override
	public Employee selectEmployee(Employee employeeCode) {
		return dao.selectEmployee(employeeCode);
	}
	
	@Override
	public Employee selectEmployeeSt(String employeeCode) {
		return dao.selectEmployeeSt(employeeCode);
	}
	
	@Override
	public int insertEmployee(Employee edt) {
		return dao.insertEmployee(edt);
	}
	
	@Override
	public List<Employee> selectEmployeeAll() {
		return dao.selectEmployeeAll();
	}
	
	@Override
	public int updateEmployee(Employee emp) {
		return dao.updateEmployee(emp);
	}
	
	@Override
	public int deleteEmployee(String emp) {
		return dao.deleteEmployee(emp);
	}
}
