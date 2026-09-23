package com.attendance.system.admin.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class AdminJeongmoCreateRequest {

	private LocalDate jeongmoDate;
	private LocalTime startTime;
	private LocalTime endTime;
	private String place;

	public LocalDate getJeongmoDate() {
		return jeongmoDate;
	}

	public void setJeongmoDate(LocalDate jeongmoDate) {
		this.jeongmoDate = jeongmoDate;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}
}
