package com.attendance.system.admin.dto;

public class AdminMileageChange {

	private String memberId;
	private String actionType;
	private Integer points;
	private String category;
	private String detailReason;

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDetailReason() {
		return detailReason;
	}

	public void setDetailReason(String detailReason) {
		this.detailReason = detailReason;
	}
}
