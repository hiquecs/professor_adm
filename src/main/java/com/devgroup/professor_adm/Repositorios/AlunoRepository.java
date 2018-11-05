package com.devgroup.professor_adm.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.devgroup.professor_adm.dominio.Aluno;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Integer> {

	@Transactional(readOnly = true)
	Aluno findByEmail(String email);

}
