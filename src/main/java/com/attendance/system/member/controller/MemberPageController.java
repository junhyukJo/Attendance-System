package com.attendance.system.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberPageController {

	@GetMapping({"/", "/member"})
	public String memberPage() {
		return "member";
	}

	@GetMapping("/member/signup")
	public String signupPage() {
		return "member-signup";
	}
}
