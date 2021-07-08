package com.example.demo.service;

import com.example.demo.entity.Employee;

public interface AttendanceEmpService {
	Employee loginEmployee(String id);
	boolean setStartTime(Employee employee);
	boolean setFinishTime(Employee employee);
}
