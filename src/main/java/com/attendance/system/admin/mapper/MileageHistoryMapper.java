package com.attendance.system.admin.mapper;

import java.util.List;

import com.attendance.system.admin.dto.AdminMileageHistorySearchCondition;
import com.attendance.system.admin.domain.MileageHistory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MileageHistoryMapper {

	List<MileageHistory> findHistories(AdminMileageHistorySearchCondition condition);

	int insert(MileageHistory mileageHistory);

	int deleteByMemberId(String memberId);
}
