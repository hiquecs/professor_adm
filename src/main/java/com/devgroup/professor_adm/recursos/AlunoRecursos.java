package com.devgroup.professor_adm.recursos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.devgroup.professor_adm.dominio.Aluno;
import com.devgroup.professor_adm.servicos.AlunoServicos;

@Controller
@RequestMapping(value="/alunos")
public class AlunoRecursos {
	
	@Autowired
	private AlunoServicos service;
	

	@RequestMapping("/{id}")
	public String preEditar(@PathVariable("id") Integer id, ModelMap model) {
		
		//Obtendo o registro do aluno a ser editado
		Aluno aluno = service.buscarPorID(id);
		
		model.addAttribute("aluno", aluno);
		
		return "/aluno/form";
	}
	

}
