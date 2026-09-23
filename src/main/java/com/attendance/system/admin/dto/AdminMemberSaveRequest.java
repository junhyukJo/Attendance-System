package com.attendance.system.admin.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AdminMemberSaveRequest {

	private String memberId;
	private String name;
	private Boolean active;
	private LocalDate birthDate;
	private String grade;
	private BigDecimal warn;
	private Integer mileageScore;

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public BigDecimal getWarn() {
		return warn;
	}

	public void setWarn(BigDecimal warn) {
		this.warn = warn;
	}

	public Integer getMileageScore() {
		return mileageScore;
	}

	public void setMileageScore(Integer mileageScore) {
		this.mileageScore = mileageScore;
	}
}
