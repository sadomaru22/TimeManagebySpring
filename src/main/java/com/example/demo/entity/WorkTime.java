package com.example.demo.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.relational.core.mapping.Table;

import lombok.RequiredArgsConstructor;

@Table("worktime")
@RequiredArgsConstructor
@Configuration
public class WorkTime {

	/**
	 * 出勤日。
	 */
	private LocalDate workDate;
	/**
	 * 出勤時刻。
	 */
	private LocalTime startTime;
	/**
	 * 退勤時刻。
	 */
	private LocalTime finishTime;
	
	/**
	 * 勤務時間(自動計算する)
	 */
	private int workingHours;
	
	private int workingMins;
	
	/*
	 * 従業員テーブルと紐づけるためのコード
	 */
	private String employee_code;
	
//	public WorkTimeDTO(LocalTime startTime, LocalTime finishTime) {
//		this.startTime = startTime;
//		this.finishTime = finishTime;
//	}
//	
//	public WorkTimeDTO(LocalDate workDate, LocalTime startTime, LocalTime finishTime) {
//		this.workDate = workDate;
//		this.startTime = startTime;
//		this.finishTime = finishTime;
//	}

	public String getEmployee_code() {
		return employee_code;
	}

	public void setEmployee_code(String employee_code) {
		this.employee_code = employee_code;
	}

	public LocalDate getWorkDate() {
		return workDate;
	}

	public void setWorkDate(LocalDate workDate) {
		this.workDate = workDate;
	}


	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}


	public LocalTime getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(LocalTime finishTime) {
		this.finishTime = finishTime;
	}


	public int getWorkingHours() {
		return workingHours;
	}

	public int getWorkingMins() {
		return workingMins;
	}

	public void setWorkingMins(int workingMins) {
		this.workingMins = workingMins;
	}

	public void setWorkingHours(int workingHours) {
		this.workingHours = workingHours;
	}


	
	public void calcWorkingHours() {
		//LocalTime dfinishTime = finishTime.minus(1, ChronoUnit.HOURS);
		
		//Duration duration = Duration.between(startTime, dfinishTime);  //.minusHours(1);
		
		//long minutes = ChronoUnit.HOURS.between(startTime, finishTime);
		//minutes.format(DateTimeFormatter.ofPattern("HH:mm"));
		
		int shours = startTime.getHour();
		int smins = startTime.getMinute();
		
		int fhours = finishTime.getHour();
		int fmins = finishTime.getMinute();
		
//		LocalTime sTime = LocalTime.of(shours, smins);
//		LocalTime fTime = LocalTime.of(fhours -1, fmins);
//		
//		long minutes = ChronoUnit.MINUTES.between(fTime, sTime);
//		
//		long hours = minutes / 60;
//		long mins = minutes % 60;
		
		int diff = (fhours * 60 + fmins) - (shours * 60 + smins) - 60;
		int diffHour = diff / 60;   //商
		int diffMins = diff % 60;  //あまり
		
		System.out.println(diffHour);
		System.out.println(diffMins);
		
		//Duration duration = Duration.between(finishTime, startTime);
		
		
		
		setWorkingHours(diffHour);
		setWorkingMins(diffMins);
	}

}