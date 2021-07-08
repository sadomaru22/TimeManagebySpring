package com.example.demo.dao;

import com.example.demo.entity.Employee;

public interface AttendanceEmployeeDao {
	Employee loginEmployee(String id);
	boolean setStartTime(Employee employee);
	boolean setFinishTime(Employee employee);
}
