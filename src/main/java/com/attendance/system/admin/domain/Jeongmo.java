package com.attendance.system.admin.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Jeongmo {

	private String jeongmoId;
	private LocalDate jeongmoDate;
	private LocalTime startTime;
	private LocalTime endTime;
	private String place;
	private Integer requiredCount;
	private Integer actualCount;
	private LocalDateTime regDtm;
	private LocalDateTime chgDtm;

	public String getJeongmoId() {
		return jeongmoId;
	}

	public void setJeongmoId(String jeongmoId) {
		this.jeongmoId = jeongmoId;
	}

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

	public Integer getRequiredCount() {
		return requiredCount;
	}

	public void setRequiredCount(Integer requiredCount) {
		this.requiredCount = requiredCount;
	}

	public Integer getActualCount() {
		return actualCount;
	}

	public void setActualCount(Integer actualCount) {
		this.actualCount = actualCount;
	}

	public LocalDateTime getRegDtm() {
		return regDtm;
	}

	public void setRegDtm(LocalDateTime regDtm) {
		this.regDtm = regDtm;
	}

	public LocalDateTime getChgDtm() {
		return chgDtm;
	}

	public void setChgDtm(LocalDateTime chgDtm) {
		this.chgDtm = chgDtm;
	}
}
