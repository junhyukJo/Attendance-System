package com.attendance.system.member.controller;

import java.time.LocalDate;

import com.attendance.system.member.domain.AttendanceRecord;
import com.attendance.system.member.mapper.AttendanceRecordMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MemberAttendanceController {

	private final AttendanceRecordMapper attendanceRecordMapper;

	public MemberAttendanceController(AttendanceRecordMapper attendanceRecordMapper) {
		this.attendanceRecordMapper = attendanceRecordMapper;
	}

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
		AttendanceRecord attendanceRecord = new AttendanceRecord(name, birthDate);
		attendanceRecordMapper.insert(attendanceRecord);
		model.addAttribute("name", name);
		model.addAttribute("birthDate", birthDate);
		model.addAttribute("savedAt", attendanceRecord.getRegDtm());
		return "member-attendance-complete";
	}
}
