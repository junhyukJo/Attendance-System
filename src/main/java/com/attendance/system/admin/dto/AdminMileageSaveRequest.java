package com.attendance.system.admin.dto;

import java.util.ArrayList;
import java.util.List;

public class AdminMileageSaveRequest {

	private List<AdminMileageChange> changes = new ArrayList<>();

	public List<AdminMileageChange> getChanges() {
		return changes;
	}

	public void setChanges(List<AdminMileageChange> changes) {
		this.changes = changes;
	}
}
