package com.attendance.system.admin.dto;

import java.time.LocalDate;

public class AdminMileageHistorySearchCondition {

	private String name;
	private String memberId;
	private String actionType;
	private LocalDate fromDate;
	private LocalDate toDate;

	public AdminMileageHistorySearchCondition(String name, String memberId, String actionType, LocalDate fromDate, LocalDate toDate) {
		this.name = name;
		this.memberId = memberId;
		this.actionType = actionType;
		this.fromDate = fromDate;
		this.toDate = toDate;
	}

	public String getName() {
		return name;
	}

	public String getMemberId() {
		return memberId;
	}

	public String getActionType() {
		return actionType;
	}

	public LocalDate getFromDate() {
		return fromDate;
	}

	public LocalDate getToDate() {
		return toDate;
	}
}
