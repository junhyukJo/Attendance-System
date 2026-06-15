package com.attendance.system.member.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class AttendanceRecord {

	private String id;
	private String memberId;

	private String name;

	private LocalDate birthDate;

	private LocalDateTime regDtm;

	private LocalDateTime chgDtm;

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

	public String getMemberId() {
		return memberId;
	}

	public String getName() {
		return name;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public LocalDateTime getRegDtm() {
		return regDtm;
	}

	public LocalDateTime getChgDtm() {
		return chgDtm;
	}
}
