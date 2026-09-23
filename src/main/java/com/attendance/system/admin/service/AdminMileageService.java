package com.attendance.system.admin.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.attendance.system.admin.dto.AdminMileageHistorySearchCondition;
import com.attendance.system.admin.dto.AdminAttendanceSearchCondition;
import com.attendance.system.admin.dto.AdminJeongmoCreateRequest;
import com.attendance.system.admin.dto.AdminJeongmoSearchCondition;
import com.attendance.system.admin.dto.AdminMemberSaveRequest;
import com.attendance.system.admin.dto.AdminMemberSearchCondition;
import com.attendance.system.admin.dto.AdminMileageChange;
import com.attendance.system.admin.domain.Jeongmo;
import com.attendance.system.admin.domain.Member;
import com.attendance.system.admin.domain.MileageHistory;
import com.attendance.system.admin.mapper.JeongmoMapper;
import com.attendance.system.admin.mapper.JeongmoRequiredMemberMapper;
import com.attendance.system.admin.mapper.MemberMapper;
import com.attendance.system.admin.mapper.MileageHistoryMapper;
import com.attendance.system.member.domain.AttendanceRecord;
import com.attendance.system.member.mapper.AttendanceRecordMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminMileageService {

	private static final Pattern MEMBER_ID_PATTERN = Pattern.compile("^U(\\d+)$");
	private static final Pattern JEONGMO_ID_PATTERN = Pattern.compile("^J(\\d+)$");

	private final MemberMapper memberMapper;
	private final MileageHistoryMapper mileageHistoryMapper;
	private final AttendanceRecordMapper attendanceRecordMapper;
	private final JeongmoMapper jeongmoMapper;
	private final JeongmoRequiredMemberMapper jeongmoRequiredMemberMapper;

	public AdminMileageService(
			MemberMapper memberMapper,
			MileageHistoryMapper mileageHistoryMapper,
			AttendanceRecordMapper attendanceRecordMapper,
			JeongmoMapper jeongmoMapper,
			JeongmoRequiredMemberMapper jeongmoRequiredMemberMapper
	) {
		this.memberMapper = memberMapper;
		this.mileageHistoryMapper = mileageHistoryMapper;
		this.attendanceRecordMapper = attendanceRecordMapper;
		this.jeongmoMapper = jeongmoMapper;
		this.jeongmoRequiredMemberMapper = jeongmoRequiredMemberMapper;
	}

	public List<Member> findMembers(String name, String memberId, String grade) {
		return memberMapper.findMembers(new AdminMemberSearchCondition(trimToNull(name), trimToNull(memberId), trimToNull(grade)));
	}

	@Transactional
	public Member createMember(AdminMemberSaveRequest request) {
		validateMemberRequest(request, false);

		Member member = new Member();
		member.setMemberId(generateNextMemberId());
		member.setName(request.getName().trim());
		member.setActive(request.getActive() == null ? Boolean.TRUE : request.getActive());
		member.setBirthDate(request.getBirthDate());
		member.setGrade(trimToNull(request.getGrade()));
		member.setWarn(request.getWarn());
		member.setMileageScore(request.getMileageScore() == null ? 0 : request.getMileageScore());
		member.setRegDtm(LocalDateTime.now());
		member.setChgDtm(LocalDateTime.now());
		memberMapper.insert(member);
		return member;
	}

	@Transactional
	public void updateMember(AdminMemberSaveRequest request) {
		validateMemberRequest(request, true);
		Member existing = memberMapper.findById(request.getMemberId());
		if (existing == null) {
			throw new IllegalArgumentException("존재하지 않는 회원입니다: " + request.getMemberId());
		}

		existing.setName(request.getName().trim());
		existing.setActive(request.getActive() == null ? Boolean.TRUE : request.getActive());
		existing.setBirthDate(request.getBirthDate());
		existing.setGrade(trimToNull(request.getGrade()));
		existing.setWarn(request.getWarn());
		existing.setMileageScore(request.getMileageScore() == null ? 0 : request.getMileageScore());
		memberMapper.updateMember(existing);
	}

	@Transactional
	public void updateMembers(List<AdminMemberSaveRequest> requests) {
		if (requests == null || requests.isEmpty()) {
			throw new IllegalArgumentException("저장할 회원 정보가 없습니다.");
		}
		for (AdminMemberSaveRequest request : requests) {
			updateMember(request);
		}
	}

	public List<MileageHistory> findHistories(String name, String memberId, String actionType, java.time.LocalDate fromDate, java.time.LocalDate toDate) {
		return mileageHistoryMapper.findHistories(new AdminMileageHistorySearchCondition(
				trimToNull(name),
				trimToNull(memberId),
				trimToNull(actionType),
				fromDate,
				toDate
		));
	}

	public List<AttendanceRecord> findAttendances(LocalDate jeongmoDate, LocalTime jeongmoStartTime, LocalTime jeongmoEndTime, String jeongmoPlace) {
		return attendanceRecordMapper.findAttendances(new AdminAttendanceSearchCondition(
				jeongmoDate,
				jeongmoStartTime,
				jeongmoEndTime,
				trimToNull(jeongmoPlace)
		));
	}

	public List<Jeongmo> findJeongmos(LocalDate jeongmoDate, String place) {
		return jeongmoMapper.findJeongmos(new AdminJeongmoSearchCondition(jeongmoDate, trimToNull(place)));
	}

	public Jeongmo findJeongmo(String jeongmoId) {
		String normalizedId = trimToNull(jeongmoId);
		return normalizedId == null ? null : jeongmoMapper.findById(normalizedId);
	}

	public List<Member> findRequiredMembers(String jeongmoId) {
		String normalizedId = trimToNull(jeongmoId);
		return normalizedId == null ? List.of() : jeongmoRequiredMemberMapper.findMembersByJeongmoId(normalizedId);
	}

	public List<AttendanceRecord> findActualAttendancesByJeongmo(String jeongmoId) {
		String normalizedId = trimToNull(jeongmoId);
		return normalizedId == null ? List.of() : attendanceRecordMapper.findByJeongmoId(normalizedId);
	}

	public List<Member> findAllMembers() {
		return memberMapper.findMembers(new AdminMemberSearchCondition(null, null, null));
	}

	@Transactional
	public Jeongmo createJeongmo(AdminJeongmoCreateRequest request) {
		validateJeongmoRequest(request);

		Jeongmo jeongmo = new Jeongmo();
		jeongmo.setJeongmoId(generateNextJeongmoId());
		jeongmo.setJeongmoDate(request.getJeongmoDate());
		jeongmo.setStartTime(request.getStartTime());
		jeongmo.setEndTime(request.getEndTime());
		jeongmo.setPlace(request.getPlace().trim());
		jeongmo.setRegDtm(LocalDateTime.now());
		jeongmo.setChgDtm(LocalDateTime.now());
		jeongmoMapper.insert(jeongmo);
		attendanceRecordMapper.linkToJeongmo(
				jeongmo.getJeongmoId(),
				jeongmo.getJeongmoDate(),
				jeongmo.getStartTime(),
				jeongmo.getEndTime(),
				jeongmo.getPlace()
		);
		return jeongmo;
	}

	@Transactional
	public void deleteJeongmo(String jeongmoId) {
		Jeongmo jeongmo = requireJeongmo(jeongmoId);
		attendanceRecordMapper.clearJeongmoId(jeongmo.getJeongmoId());
		jeongmoRequiredMemberMapper.deleteByJeongmoId(jeongmo.getJeongmoId());
		jeongmoMapper.deleteById(jeongmo.getJeongmoId());
	}

	@Transactional
	public void addRequiredMember(String jeongmoId, String memberId) {
		Jeongmo jeongmo = requireJeongmo(jeongmoId);
		Member member = requireMember(memberId);
		jeongmoRequiredMemberMapper.insertIfAbsent(jeongmo.getJeongmoId(), member.getMemberId());
	}

	@Transactional
	public void deleteRequiredMembers(String jeongmoId, List<String> memberIds) {
		Jeongmo jeongmo = requireJeongmo(jeongmoId);
		Set<String> normalizedIds = normalizeIds(memberIds, "삭제할 회원을 선택해 주세요.");
		for (String memberId : normalizedIds) {
			requireMember(memberId);
		}
		jeongmoRequiredMemberMapper.deleteByMemberIds(jeongmo.getJeongmoId(), List.copyOf(normalizedIds));
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

	@Transactional
	public void deleteMembers(List<String> memberIds) {
		Set<String> normalizedIds = normalizeIds(memberIds, "삭제할 회원을 선택해 주세요.");

		for (String memberId : normalizedIds) {
			requireMember(memberId);
		}

		for (String memberId : normalizedIds) {
			mileageHistoryMapper.deleteByMemberId(memberId);
			attendanceRecordMapper.deleteByMemberId(memberId);
			memberMapper.deleteById(memberId);
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

	private void validateMemberRequest(AdminMemberSaveRequest request, boolean requireId) {
		if (request == null) {
			throw new IllegalArgumentException("회원 정보가 비어 있습니다.");
		}
		if (requireId && trimToNull(request.getMemberId()) == null) {
			throw new IllegalArgumentException("회원 ID가 비어 있습니다.");
		}
		if (trimToNull(request.getName()) == null) {
			throw new IllegalArgumentException("회원명을 입력해 주세요.");
		}
		if (request.getMileageScore() != null && request.getMileageScore() < 0) {
			throw new IllegalArgumentException("마일리지는 0 이상이어야 합니다.");
		}
		if (request.getWarn() != null && request.getWarn().signum() < 0) {
			throw new IllegalArgumentException("경고횟수는 0 이상이어야 합니다.");
		}
	}

	private void validateJeongmoRequest(AdminJeongmoCreateRequest request) {
		if (request == null) {
			throw new IllegalArgumentException("정모 정보가 비어 있습니다.");
		}
		if (request.getJeongmoDate() == null) {
			throw new IllegalArgumentException("정모 날짜를 입력해 주세요.");
		}
		if (request.getStartTime() == null) {
			throw new IllegalArgumentException("정모 시작 시간을 입력해 주세요.");
		}
		if (request.getEndTime() == null) {
			throw new IllegalArgumentException("정모 종료 시간을 입력해 주세요.");
		}
		if (!request.getEndTime().isAfter(request.getStartTime())) {
			throw new IllegalArgumentException("정모 종료 시간은 시작 시간보다 늦어야 합니다.");
		}
		if (trimToNull(request.getPlace()) == null) {
			throw new IllegalArgumentException("정모 장소를 입력해 주세요.");
		}
	}

	private String generateNextMemberId() {
		int maxNumber = 0;
		for (String memberId : memberMapper.findAllMemberIds()) {
			Matcher matcher = MEMBER_ID_PATTERN.matcher(memberId == null ? "" : memberId.trim());
			if (!matcher.matches()) {
				continue;
			}
			maxNumber = Math.max(maxNumber, Integer.parseInt(matcher.group(1)));
		}
		return "U" + String.format("%04d", maxNumber + 1);
	}

	private String generateNextJeongmoId() {
		int maxNumber = 0;
		for (String jeongmoId : jeongmoMapper.findAllJeongmoIds()) {
			Matcher matcher = JEONGMO_ID_PATTERN.matcher(jeongmoId == null ? "" : jeongmoId.trim());
			if (!matcher.matches()) {
				continue;
			}
			maxNumber = Math.max(maxNumber, Integer.parseInt(matcher.group(1)));
		}
		return "J" + String.format("%04d", maxNumber + 1);
	}

	private Jeongmo requireJeongmo(String jeongmoId) {
		String normalizedId = trimToNull(jeongmoId);
		if (normalizedId == null) {
			throw new IllegalArgumentException("정모를 선택해 주세요.");
		}
		Jeongmo jeongmo = jeongmoMapper.findById(normalizedId);
		if (jeongmo == null) {
			throw new IllegalArgumentException("존재하지 않는 정모입니다: " + normalizedId);
		}
		return jeongmo;
	}

	private Member requireMember(String memberId) {
		String normalizedId = trimToNull(memberId);
		if (normalizedId == null) {
			throw new IllegalArgumentException("회원 ID가 올바르지 않습니다.");
		}
		Member member = memberMapper.findById(normalizedId);
		if (member == null) {
			throw new IllegalArgumentException("존재하지 않는 회원입니다: " + normalizedId);
		}
		return member;
	}

	private Set<String> normalizeIds(List<String> ids, String emptyMessage) {
		Set<String> normalizedIds = new LinkedHashSet<>();
		for (String id : ids) {
			String normalizedId = trimToNull(id);
			if (normalizedId == null) {
				throw new IllegalArgumentException("삭제 대상 ID가 올바르지 않습니다.");
			}
			normalizedIds.add(normalizedId);
		}
		if (normalizedIds.isEmpty()) {
			throw new IllegalArgumentException(emptyMessage);
		}
		return normalizedIds;
	}

	private String trimToNull(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		return value.trim();
	}
}
