package com.attendance.system.admin.controller;

import java.util.List;
import java.util.Map;

import com.attendance.system.admin.AdminCredentials;
import com.attendance.system.admin.dto.AdminMileageSaveRequest;
import com.attendance.system.admin.domain.Member;
import com.attendance.system.admin.service.AdminMileageService;
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

	public AdminMileageController(AdminMileageService adminMileageService) {
		this.adminMileageService = adminMileageService;
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

	@PostMapping("/admin/mileage/save")
	public ResponseEntity<?> saveMileageChanges(@RequestBody AdminMileageSaveRequest request, HttpSession session) {
		if (!isAuthenticated(session)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "관리자 로그인이 필요합니다."));
		}
		if (request == null || request.getChanges() == null || request.getChanges().isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("message", "저장할 변경사항이 없습니다."));
		}

		try {
			adminMileageService.applyMileageChanges(request.getChanges(), AdminCredentials.ADMIN_ID);
			return ResponseEntity.ok(Map.of("message", "변경사항이 저장되었습니다."));
		}
		catch (IllegalArgumentException exception) {
			return ResponseEntity.badRequest().body(Map.of("message", exception.getMessage()));
		}
	}

	private boolean isAuthenticated(HttpSession session) {
		return Boolean.TRUE.equals(session.getAttribute(AdminCredentials.SESSION_KEY));
	}
}
