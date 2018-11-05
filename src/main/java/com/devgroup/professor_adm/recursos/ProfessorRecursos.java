package com.devgroup.professor_adm.recursos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.devgroup.professor_adm.Repositorios.ProfessorDao;
import com.devgroup.professor_adm.Repositorios.ProfessorRepository;
import com.devgroup.professor_adm.dominio.Professor;

@Controller
public class ProfessorRecursos {
	
	@Autowired 
	private ProfessorRepository dao;
	@Autowired 
	private ProfessorDao repo;

	@GetMapping("professor/editar/{id}")
	public String preEditar(@PathVariable("id") Integer id,ModelMap model) {
		
		//Obtendo o registro do professor a ser editado
		Professor professor = repo.buscarPorId(id);
		
		model.addAttribute("professor",professor);
		return "/professor/editar_professor";
	}
	
}
