package com.attendance.system.admin.domain;

import java.time.LocalDateTime;

public class MileageHistory {

	private String historyId;
	private String memberId;
	private String memberName;
	private String actionType;
	private Integer points;
	private String category;
	private String detailReason;
	private Integer beforeMileage;
	private Integer afterMileage;
	private String createdBy;
	private LocalDateTime regDtm;

	public String getHistoryId() {
		return historyId;
	}

	public void setHistoryId(String historyId) {
		this.historyId = historyId;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
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

	public Integer getBeforeMileage() {
		return beforeMileage;
	}

	public void setBeforeMileage(Integer beforeMileage) {
		this.beforeMileage = beforeMileage;
	}

	public Integer getAfterMileage() {
		return afterMileage;
	}

	public void setAfterMileage(Integer afterMileage) {
		this.afterMileage = afterMileage;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getRegDtm() {
		return regDtm;
	}

	public void setRegDtm(LocalDateTime regDtm) {
		this.regDtm = regDtm;
	}
}
