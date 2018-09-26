package com.devgroup.professor_adm.recursos;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.devgroup.professor_adm.dominio.Aluno;

@RestController
@RequestMapping(value="/alunos")
public class AlunoRecursos {
	
	@RequestMapping (method=RequestMethod.GET)
	public List<Aluno> listar() {
		
		Aluno aluno1 = new Aluno(1,"Mario Jose","Mario@hotmail.com","1234","221564",null);
		Aluno aluno2 = new Aluno(2,"Carlos Henrique","Carlos@hotmail.com","12345","331564",null);
		
		
		List <Aluno> lista = new ArrayList<>();
		
		lista.add(aluno1);
		lista.add(aluno2);
		
		return lista ;
	}

}
