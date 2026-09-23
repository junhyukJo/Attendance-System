package com.attendance.system.admin.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.attendance.system.admin.domain.Member;
import com.attendance.system.admin.dto.AdminMemberSaveRequest;
import com.attendance.system.admin.dto.AdminMileageChange;
import com.attendance.system.admin.mapper.MemberMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class AdminMileageServiceTest {

	@Autowired
	private MemberMapper memberMapper;

	@Autowired
	private AdminMileageService adminMileageService;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	void appliesEarnMileageAndStoresHistory() {
		Member member = new Member();
		member.setMemberId("member001");
		member.setName("조준혁");
		member.setActive(true);
		member.setBirthDate(LocalDate.of(1994, 5, 10));
		member.setGrade("구A");
		member.setWarn(new BigDecimal("0.5"));
		member.setMileageScore(100);
		member.setRegDtm(LocalDateTime.now());
		member.setChgDtm(LocalDateTime.now());
		memberMapper.insert(member);

		AdminMileageChange change = new AdminMileageChange();
		change.setMemberId("member001");
		change.setActionType("EARN");
		change.setPoints(100);
		change.setCategory("기타100");
		change.setDetailReason("지인가입");

		adminMileageService.applyMileageChanges(List.of(change), "test-admin");

		Member updatedMember = memberMapper.findById("member001");
		Integer historyCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM mileage_history WHERE member_id = ?", Integer.class, "member001");

		assertThat(updatedMember.getMileageScore()).isEqualTo(200);
		assertThat(historyCount).isEqualTo(1);
	}

	@Test
	void deletesSelectedMembersWithRelatedData() {
		Member member = new Member();
		member.setMemberId("member-delete-001");
		member.setName("삭제테스트");
		member.setActive(true);
		member.setBirthDate(LocalDate.of(1994, 5, 10));
		member.setGrade("구A");
		member.setWarn(new BigDecimal("0.5"));
		member.setMileageScore(100);
		member.setRegDtm(LocalDateTime.now());
		member.setChgDtm(LocalDateTime.now());
		memberMapper.insert(member);

		jdbcTemplate.update(
				"INSERT INTO attendance_records (id, member_id, name, birth_date, reg_dtm, chg_dtm) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)",
				"attendance-delete-001",
				"member-delete-001",
				"삭제테스트",
				LocalDate.of(1994, 5, 10)
		);
		jdbcTemplate.update(
				"""
				INSERT INTO mileage_history (
				    history_id, member_id, member_name, action_type, points, category, detail_reason,
				    before_mileage, after_mileage, created_by, reg_dtm
				) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
				""",
				"history-delete-001",
				"member-delete-001",
				"삭제테스트",
				"EARN",
				100,
				"기타100",
				"삭제 검증",
				0,
				100,
				"test-admin"
		);

		adminMileageService.deleteMembers(List.of("member-delete-001"));

		Integer memberCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM members WHERE member_id = ?", Integer.class, "member-delete-001");
		Integer attendanceCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM attendance_records WHERE member_id = ?", Integer.class, "member-delete-001");
		Integer historyCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM mileage_history WHERE member_id = ?", Integer.class, "member-delete-001");

		assertThat(memberCount).isZero();
		assertThat(attendanceCount).isZero();
		assertThat(historyCount).isZero();
	}

	@Test
	void createsMemberWithGeneratedId() {
		AdminMemberSaveRequest request = new AdminMemberSaveRequest();
		request.setName("신규회원");
		request.setActive(true);
		request.setBirthDate(LocalDate.of(1999, 1, 1));
		request.setGrade("구B");
		request.setWarn(new BigDecimal("0.0"));
		request.setMileageScore(10);

		Member created = adminMileageService.createMember(request);

		assertThat(created.getMemberId()).startsWith("U");
		assertThat(memberMapper.findById(created.getMemberId())).isNotNull();
	}

	@Test
	void updatesMemberProfile() {
		Member member = new Member();
		member.setMemberId("member-update-001");
		member.setName("수정전");
		member.setActive(true);
		member.setBirthDate(LocalDate.of(1994, 5, 10));
		member.setGrade("구A");
		member.setWarn(new BigDecimal("0.5"));
		member.setMileageScore(100);
		member.setRegDtm(LocalDateTime.now());
		member.setChgDtm(LocalDateTime.now());
		memberMapper.insert(member);

		AdminMemberSaveRequest request = new AdminMemberSaveRequest();
		request.setMemberId("member-update-001");
		request.setName("수정후");
		request.setActive(false);
		request.setBirthDate(LocalDate.of(1995, 2, 20));
		request.setGrade("구C");
		request.setWarn(new BigDecimal("1.0"));
		request.setMileageScore(70);

		adminMileageService.updateMember(request);

		Member updated = memberMapper.findById("member-update-001");
		assertThat(updated.getName()).isEqualTo("수정후");
		assertThat(updated.getActive()).isFalse();
		assertThat(updated.getGrade()).isEqualTo("구C");
		assertThat(updated.getMileageScore()).isEqualTo(70);
	}
}
