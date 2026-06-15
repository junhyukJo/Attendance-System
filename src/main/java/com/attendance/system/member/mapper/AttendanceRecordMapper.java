package com.attendance.system.member.mapper;

import com.attendance.system.member.domain.AttendanceRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AttendanceRecordMapper {

	int insert(AttendanceRecord attendanceRecord);

	AttendanceRecord findById(String id);
}
