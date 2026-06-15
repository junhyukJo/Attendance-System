package com.attendance.system.admin.mapper;

import java.util.List;

import com.attendance.system.admin.dto.AdminMemberSearchCondition;
import com.attendance.system.admin.domain.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {

	List<Member> findMembers(AdminMemberSearchCondition condition);

	Member findById(String id);

	int updateMileage(@Param("id") String id, @Param("mileageScore") Integer mileageScore);

	int insert(Member member);
}
