package com.attendance.system.admin.dto;

import java.time.LocalDate;

public class AdminJeongmoSearchCondition {

	private LocalDate jeongmoDate;
	private String place;

	public AdminJeongmoSearchCondition(LocalDate jeongmoDate, String place) {
		this.jeongmoDate = jeongmoDate;
		this.place = place;
	}

	public LocalDate getJeongmoDate() {
		return jeongmoDate;
	}

	public String getPlace() {
		return place;
	}
}
