package com.attendance.system.admin.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.attendance.system.admin.dto.AdminMemberSearchCondition;
import com.attendance.system.admin.dto.AdminMileageChange;
import com.attendance.system.admin.domain.Member;
import com.attendance.system.admin.domain.MileageHistory;
import com.attendance.system.admin.mapper.MemberMapper;
import com.attendance.system.admin.mapper.MileageHistoryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminMileageService {

	private final MemberMapper memberMapper;
	private final MileageHistoryMapper mileageHistoryMapper;

	public AdminMileageService(MemberMapper memberMapper, MileageHistoryMapper mileageHistoryMapper) {
		this.memberMapper = memberMapper;
		this.mileageHistoryMapper = mileageHistoryMapper;
	}

	public List<Member> findMembers(String name, String memberId, String grade) {
		return memberMapper.findMembers(new AdminMemberSearchCondition(trimToNull(name), trimToNull(memberId), trimToNull(grade)));
	}

	@Transactional
	public void applyMileageChanges(List<AdminMileageChange> changes, String adminId) {
		for (AdminMileageChange change : changes) {
			validateChange(change);

			Member member = memberMapper.findById(change.getMemberId());
			if (member == null) {
				throw new IllegalArgumentException("존재하지 않는 회원입니다: " + change.getMemberId());
			}

			int beforeMileage = member.getMileageScore() == null ? 0 : member.getMileageScore();
			int delta = "DEDUCT".equals(change.getActionType()) ? -change.getPoints() : change.getPoints();
			int afterMileage = beforeMileage + delta;

			if (afterMileage < 0) {
				throw new IllegalArgumentException(member.getName() + " 회원의 마일리지가 0 미만이 될 수 없습니다.");
			}

			memberMapper.updateMileage(member.getMemberId(), afterMileage);

			MileageHistory history = new MileageHistory();
			history.setHistoryId(UUID.randomUUID().toString().replace("-", ""));
			history.setMemberId(member.getMemberId());
			history.setMemberName(member.getName());
			history.setActionType(change.getActionType());
			history.setPoints(change.getPoints());
			history.setCategory(change.getCategory().trim());
			history.setDetailReason(trimToNull(change.getDetailReason()));
			history.setBeforeMileage(beforeMileage);
			history.setAfterMileage(afterMileage);
			history.setCreatedBy(adminId);
			history.setRegDtm(LocalDateTime.now());
			mileageHistoryMapper.insert(history);
		}
	}

	private void validateChange(AdminMileageChange change) {
		if (change.getMemberId() == null || change.getMemberId().isBlank()) {
			throw new IllegalArgumentException("회원 ID가 비어 있습니다.");
		}
		if (!"EARN".equals(change.getActionType()) && !"DEDUCT".equals(change.getActionType())) {
			throw new IllegalArgumentException("적립/차감 구분이 올바르지 않습니다.");
		}
		if (change.getPoints() == null || change.getPoints() <= 0) {
			throw new IllegalArgumentException("점수는 1 이상이어야 합니다.");
		}
		if (change.getCategory() == null || change.getCategory().isBlank()) {
			throw new IllegalArgumentException("카테고리를 입력해 주세요.");
		}
	}

	private String trimToNull(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		return value.trim();
	}
}
