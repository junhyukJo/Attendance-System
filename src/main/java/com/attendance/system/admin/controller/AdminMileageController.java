package com.attendance.system.admin.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import com.attendance.system.admin.AdminCredentials;
import com.attendance.system.admin.domain.Jeongmo;
import com.attendance.system.admin.dto.AdminMemberBulkSaveRequest;
import com.attendance.system.admin.dto.AdminMemberDeleteRequest;
import com.attendance.system.admin.dto.AdminMemberSaveRequest;
import com.attendance.system.admin.dto.AdminJeongmoCreateRequest;
import com.attendance.system.admin.dto.AdminJeongmoDeleteRequest;
import com.attendance.system.admin.dto.AdminJeongmoRequiredMemberAddRequest;
import com.attendance.system.admin.dto.AdminJeongmoRequiredMemberDeleteRequest;
import com.attendance.system.admin.domain.MileageHistory;
import com.attendance.system.admin.dto.AdminMileageSaveRequest;
import com.attendance.system.admin.domain.Member;
import com.attendance.system.admin.service.AdminMileageService;
import com.attendance.system.member.domain.AttendanceRecord;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminMileageController {

	private final AdminMileageService adminMileageService;
	private final AdminCredentials adminCredentials;

	public AdminMileageController(AdminMileageService adminMileageService, AdminCredentials adminCredentials) {
		this.adminMileageService = adminMileageService;
		this.adminCredentials = adminCredentials;
	}

	@GetMapping("/admin/mileage")
	public String mileagePage(
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "memberId", required = false) String memberId,
			@RequestParam(value = "grade", required = false) String grade,
			HttpSession session,
			Model model
	) {
		if (!isAuthenticated(session)) {
			return "redirect:/admin/login";
		}

		List<Member> members = adminMileageService.findMembers(name, memberId, grade);
		model.addAttribute("members", members);
		model.addAttribute("searchName", name == null ? "" : name);
		model.addAttribute("searchMemberId", memberId == null ? "" : memberId);
		model.addAttribute("searchGrade", grade == null ? "" : grade);
		return "admin-mileage";
	}

	@GetMapping("/admin/mileage/history")
	public String mileageHistoryPage(
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "memberId", required = false) String memberId,
			@RequestParam(value = "actionType", required = false) String actionType,
			@RequestParam(value = "fromDate", required = false) LocalDate fromDate,
			@RequestParam(value = "toDate", required = false) LocalDate toDate,
			HttpSession session,
			Model model
	) {
		if (!isAuthenticated(session)) {
			return "redirect:/admin/login";
		}

		List<MileageHistory> histories = adminMileageService.findHistories(name, memberId, actionType, fromDate, toDate);
		model.addAttribute("histories", histories);
		model.addAttribute("searchName", name == null ? "" : name);
		model.addAttribute("searchMemberId", memberId == null ? "" : memberId);
		model.addAttribute("searchActionType", actionType == null ? "" : actionType);
		model.addAttribute("searchFromDate", fromDate);
		model.addAttribute("searchToDate", toDate);
		return "admin-mileage-history";
	}

	@GetMapping("/admin/members")
	public String membersPage(
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "memberId", required = false) String memberId,
			@RequestParam(value = "grade", required = false) String grade,
			HttpSession session,
			Model model
	) {
		if (!isAuthenticated(session)) {
			return "redirect:/admin/login";
		}

		List<Member> members = adminMileageService.findMembers(name, memberId, grade);
		model.addAttribute("members", members);
		model.addAttribute("searchName", name == null ? "" : name);
		model.addAttribute("searchMemberId", memberId == null ? "" : memberId);
		model.addAttribute("searchGrade", grade == null ? "" : grade);
		return "admin-members";
	}

	@GetMapping("/admin/attendance")
	public String attendancePage(
			@RequestParam(value = "jeongmoDate", required = false) LocalDate jeongmoDate,
			@RequestParam(value = "jeongmoStartTime", required = false) LocalTime jeongmoStartTime,
			@RequestParam(value = "jeongmoEndTime", required = false) LocalTime jeongmoEndTime,
			@RequestParam(value = "jeongmoPlace", required = false) String jeongmoPlace,
			HttpSession session,
			Model model
	) {
		if (!isAuthenticated(session)) {
			return "redirect:/admin/login";
		}

		List<AttendanceRecord> attendances = adminMileageService.findAttendances(jeongmoDate, jeongmoStartTime, jeongmoEndTime, jeongmoPlace);
		model.addAttribute("attendances", attendances);
		model.addAttribute("searchJeongmoDate", jeongmoDate);
		model.addAttribute("searchJeongmoStartTime", jeongmoStartTime);
		model.addAttribute("searchJeongmoEndTime", jeongmoEndTime);
		model.addAttribute("searchJeongmoPlace", jeongmoPlace == null ? "" : jeongmoPlace);
		model.addAttribute("attendanceCount", attendances.size());
		model.addAttribute("attendanceEntryUrlTemplate", "/member/attendance/new");
		return "admin-attendance";
	}

	@GetMapping("/admin/jeongmos")
	public String jeongmoPage(
			@RequestParam(value = "jeongmoDate", required = false) LocalDate jeongmoDate,
			@RequestParam(value = "place", required = false) String place,
			@RequestParam(value = "selectedJeongmoId", required = false) String selectedJeongmoId,
			HttpSession session,
			Model model
	) {
		if (!isAuthenticated(session)) {
			return "redirect:/admin/login";
		}

		List<Jeongmo> jeongmos = adminMileageService.findJeongmos(jeongmoDate, place);
		Jeongmo selectedJeongmo = selectedJeongmoId == null || selectedJeongmoId.isBlank()
				? (jeongmos.isEmpty() ? null : jeongmos.get(0))
				: adminMileageService.findJeongmo(selectedJeongmoId);

		model.addAttribute("jeongmos", jeongmos);
		model.addAttribute("selectedJeongmo", selectedJeongmo);
		model.addAttribute("requiredMembers", selectedJeongmo == null ? List.of() : adminMileageService.findRequiredMembers(selectedJeongmo.getJeongmoId()));
		model.addAttribute("actualAttendances", selectedJeongmo == null ? List.of() : adminMileageService.findActualAttendancesByJeongmo(selectedJeongmo.getJeongmoId()));
		model.addAttribute("memberOptions", adminMileageService.findAllMembers());
		model.addAttribute("searchJeongmoDate", jeongmoDate);
		model.addAttribute("searchPlace", place == null ? "" : place);
		return "admin-jeongmos";
	}

	@PostMapping("/admin/mileage/save")
	public ResponseEntity<?> saveMileageChanges(@RequestBody AdminMileageSaveRequest request, HttpSession session) {
		if (!isAuthenticated(session)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "관리자 로그인이 필요합니다."));
		}
		if (request == null || request.getChanges() == null || request.getChanges().isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("message", "저장할 변경사항이 없습니다."));
		}

		try {
			adminMileageService.applyMileageChanges(request.getChanges(), adminCredentials.getAdminId());
			return ResponseEntity.ok(Map.of("message", "변경사항이 저장되었습니다."));
		}
		catch (IllegalArgumentException exception) {
			return ResponseEntity.badRequest().body(Map.of("message", exception.getMessage()));
		}
	}

	@PostMapping("/admin/mileage/delete")
	public ResponseEntity<?> deleteMembers(@RequestBody AdminMemberDeleteRequest request, HttpSession session) {
		if (!isAuthenticated(session)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "관리자 로그인이 필요합니다."));
		}
		if (request == null || request.getMemberIds() == null || request.getMemberIds().isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("message", "삭제할 회원을 선택해 주세요."));
		}

		try {
			adminMileageService.deleteMembers(request.getMemberIds());
			return ResponseEntity.ok(Map.of("message", "선택한 회원이 삭제되었습니다."));
		}
		catch (IllegalArgumentException exception) {
			return ResponseEntity.badRequest().body(Map.of("message", exception.getMessage()));
		}
	}

	@PostMapping("/admin/members/create")
	public ResponseEntity<?> createMember(@RequestBody AdminMemberSaveRequest request, HttpSession session) {
		if (!isAuthenticated(session)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "관리자 로그인이 필요합니다."));
		}

		try {
			Member member = adminMileageService.createMember(request);
			return ResponseEntity.ok(Map.of(
					"message", "회원이 추가되었습니다.",
					"memberId", member.getMemberId()
			));
		}
		catch (IllegalArgumentException exception) {
			return ResponseEntity.badRequest().body(Map.of("message", exception.getMessage()));
		}
	}

	@PostMapping("/admin/members/update")
	public ResponseEntity<?> updateMember(@RequestBody AdminMemberSaveRequest request, HttpSession session) {
		if (!isAuthenticated(session)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "관리자 로그인이 필요합니다."));
		}

		try {
			adminMileageService.updateMember(request);
			return ResponseEntity.ok(Map.of("message", "회원 정보가 수정되었습니다."));
		}
		catch (IllegalArgumentException exception) {
			return ResponseEntity.badRequest().body(Map.of("message", exception.getMessage()));
		}
	}

	@PostMapping("/admin/members/save")
	public ResponseEntity<?> saveMembers(@RequestBody AdminMemberBulkSaveRequest request, HttpSession session) {
		if (!isAuthenticated(session)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "관리자 로그인이 필요합니다."));
		}
		if (request == null || request.getMembers() == null || request.getMembers().isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("message", "저장할 회원 정보가 없습니다."));
		}

		try {
			adminMileageService.updateMembers(request.getMembers());
			return ResponseEntity.ok(Map.of("message", "회원 정보가 저장되었습니다."));
		}
		catch (IllegalArgumentException exception) {
			return ResponseEntity.badRequest().body(Map.of("message", exception.getMessage()));
		}
	}

	@PostMapping("/admin/members/delete")
	public ResponseEntity<?> deleteMembersFromRegistry(@RequestBody AdminMemberDeleteRequest request, HttpSession session) {
		if (!isAuthenticated(session)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "관리자 로그인이 필요합니다."));
		}
		if (request == null || request.getMemberIds() == null || request.getMemberIds().isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("message", "삭제할 회원을 선택해 주세요."));
		}

		try {
			adminMileageService.deleteMembers(request.getMemberIds());
			return ResponseEntity.ok(Map.of("message", "선택한 회원이 삭제되었습니다."));
		}
		catch (IllegalArgumentException exception) {
			return ResponseEntity.badRequest().body(Map.of("message", exception.getMessage()));
		}
	}

	@PostMapping("/admin/jeongmos/create")
	public ResponseEntity<?> createJeongmo(@RequestBody AdminJeongmoCreateRequest request, HttpSession session) {
		if (!isAuthenticated(session)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "관리자 로그인이 필요합니다."));
		}

		try {
			Jeongmo jeongmo = adminMileageService.createJeongmo(request);
			return ResponseEntity.ok(Map.of(
					"message", "정모가 추가되었습니다.",
					"jeongmoId", jeongmo.getJeongmoId()
			));
		}
		catch (IllegalArgumentException exception) {
			return ResponseEntity.badRequest().body(Map.of("message", exception.getMessage()));
		}
	}

	@PostMapping("/admin/jeongmos/delete")
	public ResponseEntity<?> deleteJeongmo(@RequestBody AdminJeongmoDeleteRequest request, HttpSession session) {
		if (!isAuthenticated(session)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "관리자 로그인이 필요합니다."));
		}

		try {
			adminMileageService.deleteJeongmo(request == null ? null : request.getJeongmoId());
			return ResponseEntity.ok(Map.of("message", "정모가 삭제되었습니다."));
		}
		catch (IllegalArgumentException exception) {
			return ResponseEntity.badRequest().body(Map.of("message", exception.getMessage()));
		}
	}

	@PostMapping("/admin/jeongmos/required-members/add")
	public ResponseEntity<?> addRequiredMember(@RequestBody AdminJeongmoRequiredMemberAddRequest request, HttpSession session) {
		if (!isAuthenticated(session)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "관리자 로그인이 필요합니다."));
		}

		try {
			adminMileageService.addRequiredMember(
					request == null ? null : request.getJeongmoId(),
					request == null ? null : request.getMemberId()
			);
			return ResponseEntity.ok(Map.of("message", "출석필요인원이 추가되었습니다."));
		}
		catch (IllegalArgumentException exception) {
			return ResponseEntity.badRequest().body(Map.of("message", exception.getMessage()));
		}
	}

	@PostMapping("/admin/jeongmos/required-members/delete")
	public ResponseEntity<?> deleteRequiredMembers(@RequestBody AdminJeongmoRequiredMemberDeleteRequest request, HttpSession session) {
		if (!isAuthenticated(session)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "관리자 로그인이 필요합니다."));
		}

		try {
			adminMileageService.deleteRequiredMembers(
					request == null ? null : request.getJeongmoId(),
					request == null ? List.of() : request.getMemberIds()
			);
			return ResponseEntity.ok(Map.of("message", "선택한 출석필요인원이 삭제되었습니다."));
		}
		catch (IllegalArgumentException exception) {
			return ResponseEntity.badRequest().body(Map.of("message", exception.getMessage()));
		}
	}

	private boolean isAuthenticated(HttpSession session) {
		return Boolean.TRUE.equals(session.getAttribute(AdminCredentials.SESSION_KEY));
	}
}
