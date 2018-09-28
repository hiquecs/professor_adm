package com.devgroup.professor_adm.recursos;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginAlunoRecursos {
	
	@RequestMapping("/loginaluno")
	public String index() {
		return "/aluno/login";
	}
	

}
