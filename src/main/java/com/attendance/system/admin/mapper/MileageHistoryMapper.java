package com.attendance.system.admin.mapper;

import com.attendance.system.admin.domain.MileageHistory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MileageHistoryMapper {

	int insert(MileageHistory mileageHistory);
}
