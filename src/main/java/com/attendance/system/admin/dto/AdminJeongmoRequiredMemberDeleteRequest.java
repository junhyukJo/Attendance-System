package com.attendance.system.admin.dto;

import java.util.List;

public class AdminJeongmoRequiredMemberDeleteRequest {

	private String jeongmoId;
	private List<String> memberIds;

	public String getJeongmoId() {
		return jeongmoId;
	}

	public void setJeongmoId(String jeongmoId) {
		this.jeongmoId = jeongmoId;
	}

	public List<String> getMemberIds() {
		return memberIds;
	}

	public void setMemberIds(List<String> memberIds) {
		this.memberIds = memberIds;
	}
}
