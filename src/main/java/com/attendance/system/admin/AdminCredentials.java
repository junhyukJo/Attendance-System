package com.attendance.system.admin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public final class AdminCredentials {

	public static final String SESSION_KEY = "ADMIN_AUTHENTICATED";

	private final String adminId;
	private final String adminPassword;

	public AdminCredentials(
			@Value("${admin.id:}") String adminId,
			@Value("${admin.password:}") String adminPassword
	) {
		if (adminId == null || adminId.isBlank() || adminPassword == null || adminPassword.isBlank()) {
			throw new IllegalStateException("ADMIN_ID와 ADMIN_PASSWORD 환경변수를 설정해야 합니다.");
		}
		this.adminId = adminId;
		this.adminPassword = adminPassword;
	}

	public String getAdminId() {
		return adminId;
	}

	public boolean matches(String inputId, String inputPassword) {
		return adminId.equals(inputId) && adminPassword.equals(inputPassword);
	}
}
