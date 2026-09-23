package com.attendance.system.admin.mapper;

import java.util.List;

import com.attendance.system.admin.domain.Jeongmo;
import com.attendance.system.admin.dto.AdminJeongmoSearchCondition;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JeongmoMapper {

	List<Jeongmo> findJeongmos(AdminJeongmoSearchCondition condition);

	Jeongmo findById(String jeongmoId);

	List<String> findAllJeongmoIds();

	int insert(Jeongmo jeongmo);

	int deleteById(String jeongmoId);
}
