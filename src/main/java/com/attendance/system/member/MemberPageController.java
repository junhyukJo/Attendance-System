package com.attendance.system.member;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberPageController {

	@GetMapping({"/", "/member"})
	public String memberPage() {
		return "member";
	}
}
