package com.attendance.system.admin.controller;

import com.attendance.system.admin.AdminCredentials;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminAuthController {

	@GetMapping("/admin/login")
	public String loginPage(HttpSession session) {
		if (Boolean.TRUE.equals(session.getAttribute(AdminCredentials.SESSION_KEY))) {
			return "redirect:/admin/mileage";
		}
		return "admin-login";
	}

	@PostMapping("/admin/login")
	public String login(
			@RequestParam("adminId") String adminId,
			@RequestParam("password") String password,
			HttpSession session,
			RedirectAttributes redirectAttributes
	) {
		if (AdminCredentials.ADMIN_ID.equals(adminId) && AdminCredentials.ADMIN_PASSWORD.equals(password)) {
			session.setAttribute(AdminCredentials.SESSION_KEY, true);
			return "redirect:/admin/mileage";
		}

		redirectAttributes.addFlashAttribute("loginError", "관리자 ID 또는 비밀번호가 올바르지 않습니다.");
		return "redirect:/admin/login";
	}

	@PostMapping("/admin/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/admin/login";
	}
}
