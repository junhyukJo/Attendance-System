package com.attendance.system.admin.dto;

import java.util.List;

public class AdminMemberDeleteRequest {

	private List<String> memberIds;

	public List<String> getMemberIds() {
		return memberIds;
	}

	public void setMemberIds(List<String> memberIds) {
		this.memberIds = memberIds;
	}
}
