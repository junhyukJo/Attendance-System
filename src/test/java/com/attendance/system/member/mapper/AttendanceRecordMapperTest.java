package com.attendance.system.member.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.attendance.system.admin.dto.AdminAttendanceSearchCondition;
import com.attendance.system.member.domain.AttendanceRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class AttendanceRecordMapperTest {

	@Autowired
	private AttendanceRecordMapper attendanceRecordMapper;

	@Test
	void insertsAndFindsAttendanceRecord() {
		AttendanceRecord toSave = new AttendanceRecord("홍길동", LocalDate.of(2000, 1, 1));
		toSave.setJeongmoDate(LocalDate.of(2026, 6, 25));
		toSave.setJeongmoStartTime(LocalTime.of(19, 0));
		toSave.setJeongmoEndTime(LocalTime.of(22, 0));
		toSave.setJeongmoPlace("강남스포츠문화센터");

		int insertedRows = attendanceRecordMapper.insert(toSave);
		AttendanceRecord found = attendanceRecordMapper.findById(toSave.getId());

		assertThat(insertedRows).isEqualTo(1);
		assertThat(found).isNotNull();
		assertThat(found.getId()).isEqualTo(toSave.getId());
		assertThat(found.getName()).isEqualTo("홍길동");
		assertThat(found.getBirthDate()).isEqualTo(LocalDate.of(2000, 1, 1));
		assertThat(found.getJeongmoDate()).isEqualTo(LocalDate.of(2026, 6, 25));
		assertThat(found.getJeongmoStartTime()).isEqualTo(LocalTime.of(19, 0));
		assertThat(found.getJeongmoEndTime()).isEqualTo(LocalTime.of(22, 0));
		assertThat(found.getJeongmoPlace()).isEqualTo("강남스포츠문화센터");
		assertThat(found.getRegDtm()).isNotNull();
		assertThat(found.getChgDtm()).isNotNull();
	}

	@Test
	void findsAttendancesByJeongmoCondition() {
		AttendanceRecord toSave = new AttendanceRecord("김정모", LocalDate.of(1998, 3, 15));
		toSave.setJeongmoDate(LocalDate.of(2026, 6, 25));
		toSave.setJeongmoStartTime(LocalTime.of(19, 0));
		toSave.setJeongmoEndTime(LocalTime.of(22, 0));
		toSave.setJeongmoPlace("강남스포츠문화센터");
		attendanceRecordMapper.insert(toSave);

		List<AttendanceRecord> found = attendanceRecordMapper.findAttendances(
				new AdminAttendanceSearchCondition(
						LocalDate.of(2026, 6, 25),
						LocalTime.of(19, 0),
						LocalTime.of(22, 0),
						"강남스포츠문화센터"
				)
		);

		assertThat(found).extracting(AttendanceRecord::getId).contains(toSave.getId());
	}
}
