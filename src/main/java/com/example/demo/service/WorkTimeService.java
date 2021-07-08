package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Employee;
import com.example.demo.entity.WorkTime;


public interface WorkTimeService {
	String selectStartTime(Employee employee);
	String selectFinishTime(Employee employee);
	List<WorkTime> selectWorkTimeThisMonthList(Employee employee,String thisMonth);
	int lastDay();
	WorkTime selectWorkTime(String ec, String thisDate);
	WorkTime updateWorkTime(WorkTime wdto, String day);
}
