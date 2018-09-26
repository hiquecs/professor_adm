package com.devgroup.professor_adm.recursos;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.devgroup.professor_adm.dominio.Professor;

@RestController
@RequestMapping(value="/professores")
public class ProfessorRecursos {
	
	@RequestMapping (method=RequestMethod.GET)
	public List<Professor> listar() {
		
		Professor prof1 = new Professor(1,"Maria do Bairro","maria@hotmail.com","1010");
		Professor prof2 = new Professor(1,"Mario Luiz","marioluiz@hotmail.com","1011");
		
		List <Professor> lista = new ArrayList<>();
		
		lista.add(prof1);
		lista.add(prof2);
		
		return lista ;
	}

}
