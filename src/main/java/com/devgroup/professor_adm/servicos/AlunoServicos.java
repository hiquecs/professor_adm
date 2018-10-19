package com.devgroup.professor_adm.servicos;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.devgroup.professor_adm.Repositorios.AlunoDAO;
import com.devgroup.professor_adm.dominio.Aluno;

@Service
public class AlunoServicos {

	@Autowired
	private AlunoDAO dao;

	public Aluno buscarPorEmail(String email) {

		Aluno obj = dao.findByEmail(email);
		return obj;
	}

	public List<Aluno> buscarTodosAlunos() {

		return dao.findAll();
	}

	public Aluno buscarPorID(Integer id) {

		Optional<Aluno> obj = dao.findById(id);

		return obj.orElseThrow(null);
	}

	public void deletarAlunoPorId(Integer id) {
		dao.deleteById(id);
	}
}
