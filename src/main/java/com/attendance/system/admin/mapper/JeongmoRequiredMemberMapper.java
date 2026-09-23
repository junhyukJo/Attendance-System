package com.attendance.system.admin.mapper;

import java.util.List;

import com.attendance.system.admin.domain.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface JeongmoRequiredMemberMapper {

	List<Member> findMembersByJeongmoId(String jeongmoId);

	int insertIfAbsent(@Param("jeongmoId") String jeongmoId, @Param("memberId") String memberId);

	int deleteByMemberIds(@Param("jeongmoId") String jeongmoId, @Param("memberIds") List<String> memberIds);

	int deleteByJeongmoId(String jeongmoId);
}
