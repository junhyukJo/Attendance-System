package com.attendance.system.admin.dto;

public class AdminMemberSearchCondition {

	private String name;
	private String memberId;
	private String grade;

	public AdminMemberSearchCondition() {
	}

	public AdminMemberSearchCondition(String name, String memberId, String grade) {
		this.name = name;
		this.memberId = memberId;
		this.grade = grade;
	}

	public String getName() {
		return name;
	}

	public String getMemberId() {
		return memberId;
	}

	public String getGrade() {
		return grade;
	}
}
