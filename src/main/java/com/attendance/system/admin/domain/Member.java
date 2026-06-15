package com.attendance.system.admin.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Member {

	private String memberId;
	private String name;
	private Boolean active;
	private LocalDate birthDate;
	private String grade;
	private BigDecimal warn;
	private Integer mileageScore;
	private LocalDateTime regDtm;
	private LocalDateTime chgDtm;

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
