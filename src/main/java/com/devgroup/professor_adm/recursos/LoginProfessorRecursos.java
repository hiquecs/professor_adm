package com.devgroup.professor_adm.recursos;


	
	import org.springframework.stereotype.Controller;
	import org.springframework.web.bind.annotation.RequestMapping;

	@Controller
	public class LoginProfessorRecursos {
		
		@RequestMapping("/loginprofessor")
		public String index() {
			return "/professor/login";
		}
		
	}


