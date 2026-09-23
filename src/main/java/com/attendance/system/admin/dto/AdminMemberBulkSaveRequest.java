package com.attendance.system.admin.dto;

import java.util.List;

public class AdminMemberBulkSaveRequest {

	private List<AdminMemberSaveRequest> members;

	public List<AdminMemberSaveRequest> getMembers() {
		return members;
	}

	public void setMembers(List<AdminMemberSaveRequest> members) {
		this.members = members;
	}
}
