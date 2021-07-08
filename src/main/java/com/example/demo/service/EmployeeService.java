package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Employee;

public interface EmployeeService {
	Employee selectEmployee(Employee employeeCode);
	Employee selectEmployeeSt(String employeeCode);
	int insertEmployee(Employee edt);
	List<Employee> selectEmployeeAll();
	int updateEmployee(Employee emp);
	int deleteEmployee(String emp);
}
