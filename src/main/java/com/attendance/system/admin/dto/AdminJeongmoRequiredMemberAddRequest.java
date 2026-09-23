package com.attendance.system.admin.dto;

public class AdminJeongmoRequiredMemberAddRequest {

	private String jeongmoId;
	private String memberId;

	public String getJeongmoId() {
		return jeongmoId;
	}

	public void setJeongmoId(String jeongmoId) {
		this.jeongmoId = jeongmoId;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
}
