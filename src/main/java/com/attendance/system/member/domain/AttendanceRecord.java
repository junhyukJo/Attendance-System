package com.attendance.system.member.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class AttendanceRecord {

	private String id;
	private String memberId;
	private String jeongmoId;

	private String name;

	private LocalDate birthDate;

	private LocalDateTime regDtm;

	private LocalDateTime chgDtm;

	private LocalDate jeongmoDate;

	private LocalTime jeongmoStartTime;

	private LocalTime jeongmoEndTime;

	private String jeongmoPlace;

	public AttendanceRecord(String name, LocalDate birthDate) {
		this.name = name;
		this.birthDate = birthDate;
		initializeForInsert();
	}

	public AttendanceRecord() {
	}

	public void initializeForInsert() {
		if (id == null || id.isBlank()) {
			id = UUID.randomUUID().toString().replace("-", "");
		}
		if (regDtm == null) {
			regDtm = LocalDateTime.now();
		}
		if (chgDtm == null) {
			chgDtm = LocalDateTime.now();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getJeongmoId() {
		return jeongmoId;
	}

	public void setJeongmoId(String jeongmoId) {
		this.jeongmoId = jeongmoId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
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

	public LocalDate getJeongmoDate() {
		return jeongmoDate;
	}

	public void setJeongmoDate(LocalDate jeongmoDate) {
		this.jeongmoDate = jeongmoDate;
	}

	public LocalTime getJeongmoStartTime() {
		return jeongmoStartTime;
	}

	public void setJeongmoStartTime(LocalTime jeongmoStartTime) {
		this.jeongmoStartTime = jeongmoStartTime;
	}

	public LocalTime getJeongmoEndTime() {
		return jeongmoEndTime;
	}

	public void setJeongmoEndTime(LocalTime jeongmoEndTime) {
		this.jeongmoEndTime = jeongmoEndTime;
	}

	public String getJeongmoPlace() {
		return jeongmoPlace;
	}

	public void setJeongmoPlace(String jeongmoPlace) {
		this.jeongmoPlace = jeongmoPlace;
	}
}
