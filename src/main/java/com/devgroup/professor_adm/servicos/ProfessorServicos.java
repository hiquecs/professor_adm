package com.devgroup.professor_adm.servicos;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devgroup.professor_adm.Repositorios.ProfessorDAO;
import com.devgroup.professor_adm.dominio.Professor;

@Service
public class ProfessorServicos {

	@Autowired
	private ProfessorDAO dao;
	
	
	public Professor novoProfessor() {
		
		return null;
	}

	public Professor buscarPorEmail(String email) {

		Professor obj = dao.findByEmail(email);
		return obj;
	}

	public List<Professor> buscarTodosProfessores() {

		return dao.findAll();
	}

	public Professor buscarPorID(Integer id) {

		Optional<Professor> obj = dao.findById(id);

		return obj.orElseThrow(null);
	}

	public void deletarProfessorPorId(Integer id) {
		dao.deleteById(id);
	}
}
