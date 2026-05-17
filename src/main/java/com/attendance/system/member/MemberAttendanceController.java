package com.attendance.system.member;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MemberAttendanceController {

	@GetMapping("/member/attendance/new")
	public String attendanceEntryPage() {
		return "member-attendance-form";
	}

	@PostMapping("/member/attendance")
	public String submitAttendance(
			@RequestParam("name") String name,
			@RequestParam("birthDate") LocalDate birthDate,
			Model model
	) {
		model.addAttribute("name", name);
		model.addAttribute("birthDate", birthDate);
		return "member-attendance-complete";
	}
}
