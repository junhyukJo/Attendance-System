package com.attendance.system.member.controller;

import java.time.LocalDate;
import java.time.LocalTime;

import com.attendance.system.admin.domain.Jeongmo;
import com.attendance.system.admin.domain.Member;
import com.attendance.system.admin.mapper.JeongmoMapper;
import com.attendance.system.admin.mapper.MemberMapper;
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
	private final MemberMapper memberMapper;
	private final JeongmoMapper jeongmoMapper;

	public MemberAttendanceController(AttendanceRecordMapper attendanceRecordMapper, MemberMapper memberMapper, JeongmoMapper jeongmoMapper) {
		this.attendanceRecordMapper = attendanceRecordMapper;
		this.memberMapper = memberMapper;
		this.jeongmoMapper = jeongmoMapper;
	}

	@GetMapping("/member/attendance/new")
	public String attendanceEntryPage(
			@RequestParam(value = "jeongmoId", required = false) String jeongmoId,
			@RequestParam(value = "jeongmoDate", required = false) LocalDate jeongmoDate,
			@RequestParam(value = "jeongmoStartTime", required = false) LocalTime jeongmoStartTime,
			@RequestParam(value = "jeongmoEndTime", required = false) LocalTime jeongmoEndTime,
			@RequestParam(value = "jeongmoPlace", required = false) String jeongmoPlace,
			Model model
	) {
		Jeongmo jeongmo = resolveJeongmo(jeongmoId, jeongmoDate, jeongmoStartTime, jeongmoEndTime, jeongmoPlace);
		model.addAttribute("jeongmoId", jeongmo == null ? "" : jeongmo.getJeongmoId());
		model.addAttribute("jeongmoDate", jeongmo == null ? jeongmoDate : jeongmo.getJeongmoDate());
		model.addAttribute("jeongmoStartTime", jeongmo == null ? jeongmoStartTime : jeongmo.getStartTime());
		model.addAttribute("jeongmoEndTime", jeongmo == null ? jeongmoEndTime : jeongmo.getEndTime());
		model.addAttribute("jeongmoPlace", jeongmo == null ? (jeongmoPlace == null ? "" : jeongmoPlace) : jeongmo.getPlace());
		return "member-attendance-form";
	}

	@PostMapping("/member/attendance")
	public String submitAttendance(
			@RequestParam("name") String name,
			@RequestParam("birthDate") LocalDate birthDate,
			@RequestParam(value = "jeongmoId", required = false) String jeongmoId,
			@RequestParam(value = "jeongmoDate", required = false) LocalDate jeongmoDate,
			@RequestParam(value = "jeongmoStartTime", required = false) LocalTime jeongmoStartTime,
			@RequestParam(value = "jeongmoEndTime", required = false) LocalTime jeongmoEndTime,
			@RequestParam(value = "jeongmoPlace", required = false) String jeongmoPlace,
			Model model
	) {
		AttendanceRecord attendanceRecord = new AttendanceRecord(name, birthDate);
		Member member = memberMapper.findByNameAndBirthDate(name.trim(), birthDate);
		if (member != null) {
			attendanceRecord.setMemberId(member.getMemberId());
		}

		Jeongmo jeongmo = resolveJeongmo(jeongmoId, jeongmoDate, jeongmoStartTime, jeongmoEndTime, jeongmoPlace);
		if (jeongmo != null) {
			attendanceRecord.setJeongmoId(jeongmo.getJeongmoId());
			attendanceRecord.setJeongmoDate(jeongmo.getJeongmoDate());
			attendanceRecord.setJeongmoStartTime(jeongmo.getStartTime());
			attendanceRecord.setJeongmoEndTime(jeongmo.getEndTime());
			attendanceRecord.setJeongmoPlace(jeongmo.getPlace());
		}
		else {
			attendanceRecord.setJeongmoDate(jeongmoDate);
			attendanceRecord.setJeongmoStartTime(jeongmoStartTime);
			attendanceRecord.setJeongmoEndTime(jeongmoEndTime);
			attendanceRecord.setJeongmoPlace(jeongmoPlace == null || jeongmoPlace.isBlank() ? null : jeongmoPlace.trim());
		}
		attendanceRecordMapper.insert(attendanceRecord);
		model.addAttribute("name", name);
		model.addAttribute("birthDate", birthDate);
		model.addAttribute("savedAt", attendanceRecord.getRegDtm());
		return "member-attendance-complete";
	}

	private Jeongmo resolveJeongmo(String jeongmoId, LocalDate jeongmoDate, LocalTime jeongmoStartTime, LocalTime jeongmoEndTime, String jeongmoPlace) {
		if (jeongmoId != null && !jeongmoId.isBlank()) {
			return jeongmoMapper.findById(jeongmoId.trim());
		}
		if (jeongmoDate == null && jeongmoStartTime == null && jeongmoEndTime == null && (jeongmoPlace == null || jeongmoPlace.isBlank())) {
			return null;
		}
		Jeongmo jeongmo = new Jeongmo();
		jeongmo.setJeongmoDate(jeongmoDate);
		jeongmo.setStartTime(jeongmoStartTime);
		jeongmo.setEndTime(jeongmoEndTime);
		jeongmo.setPlace(jeongmoPlace == null || jeongmoPlace.isBlank() ? null : jeongmoPlace.trim());
		return jeongmo;
	}
}
