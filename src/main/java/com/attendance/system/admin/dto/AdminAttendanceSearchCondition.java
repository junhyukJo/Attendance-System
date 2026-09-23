package com.attendance.system.admin.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class AdminAttendanceSearchCondition {

	private LocalDate jeongmoDate;
	private LocalTime jeongmoStartTime;
	private LocalTime jeongmoEndTime;
	private String jeongmoPlace;

	public AdminAttendanceSearchCondition(LocalDate jeongmoDate, LocalTime jeongmoStartTime, LocalTime jeongmoEndTime, String jeongmoPlace) {
		this.jeongmoDate = jeongmoDate;
		this.jeongmoStartTime = jeongmoStartTime;
		this.jeongmoEndTime = jeongmoEndTime;
		this.jeongmoPlace = jeongmoPlace;
	}

	public LocalDate getJeongmoDate() {
		return jeongmoDate;
	}

	public LocalTime getJeongmoStartTime() {
		return jeongmoStartTime;
	}

	public LocalTime getJeongmoEndTime() {
		return jeongmoEndTime;
	}

	public String getJeongmoPlace() {
		return jeongmoPlace;
	}
}
