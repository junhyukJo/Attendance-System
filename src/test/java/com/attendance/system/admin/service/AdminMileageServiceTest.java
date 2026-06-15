package com.attendance.system.admin.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.attendance.system.admin.AdminCredentials;
import com.attendance.system.admin.domain.Member;
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

		adminMileageService.applyMileageChanges(List.of(change), AdminCredentials.ADMIN_ID);

		Member updatedMember = memberMapper.findById("member001");
		Integer historyCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM mileage_history WHERE member_id = ?", Integer.class, "member001");

		assertThat(updatedMember.getMileageScore()).isEqualTo(200);
		assertThat(historyCount).isEqualTo(1);
	}
}
