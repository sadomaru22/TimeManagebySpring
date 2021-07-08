package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.WorkTimeDao;
import com.example.demo.entity.Employee;
import com.example.demo.entity.WorkTime;

@Service
public class WorkTimeServiceImpl implements WorkTimeService {
	private WorkTimeDao dao;
	
	@Autowired
	public WorkTimeServiceImpl(WorkTimeDao dao) {
		this.dao = dao;
	}
	
	@Override
	public String selectStartTime(Employee employee) {
		return dao.selectStartTime(employee);
	}
	
	@Override
	public String selectFinishTime(Employee employee) {
		return dao.selectFinishTime(employee);
	}
	
	@Override
	public List<WorkTime> selectWorkTimeThisMonthList(Employee employee,String thisMonth) {
		return dao.selectWorkTimeThisMonthList(employee, thisMonth);
	}
	
	@Override
	public int lastDay() {
		return dao.lastDay();
	}
	
	@Override
	public WorkTime selectWorkTime(String ec, String thisDate) {
		return dao.selectWorkTime(ec, thisDate);
	}
	
	@Override
	public WorkTime updateWorkTime(WorkTime wdto, String day) {
		return dao.updateWorkTime(wdto, day);
	}
}
