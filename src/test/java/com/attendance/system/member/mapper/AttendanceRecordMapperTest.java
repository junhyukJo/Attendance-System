package com.attendance.system.member.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

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

		int insertedRows = attendanceRecordMapper.insert(toSave);
		AttendanceRecord found = attendanceRecordMapper.findById(toSave.getId());

		assertThat(insertedRows).isEqualTo(1);
		assertThat(found).isNotNull();
		assertThat(found.getId()).isEqualTo(toSave.getId());
		assertThat(found.getName()).isEqualTo("홍길동");
		assertThat(found.getBirthDate()).isEqualTo(LocalDate.of(2000, 1, 1));
		assertThat(found.getRegDtm()).isNotNull();
		assertThat(found.getChgDtm()).isNotNull();
	}
}
