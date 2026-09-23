package com.attendance.system.member.mapper;

import java.util.List;

import com.attendance.system.admin.dto.AdminAttendanceSearchCondition;
import com.attendance.system.member.domain.AttendanceRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AttendanceRecordMapper {

	int insert(AttendanceRecord attendanceRecord);

	AttendanceRecord findById(String id);

	List<AttendanceRecord> findAttendances(AdminAttendanceSearchCondition condition);

	List<AttendanceRecord> findByJeongmoId(String jeongmoId);

	int linkToJeongmo(@Param("jeongmoId") String jeongmoId,
			@Param("jeongmoDate") java.time.LocalDate jeongmoDate,
			@Param("jeongmoStartTime") java.time.LocalTime jeongmoStartTime,
			@Param("jeongmoEndTime") java.time.LocalTime jeongmoEndTime,
			@Param("jeongmoPlace") String jeongmoPlace);

	int clearJeongmoId(String jeongmoId);

	int deleteByMemberId(String memberId);
}
