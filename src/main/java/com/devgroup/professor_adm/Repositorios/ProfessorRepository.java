package com.devgroup.professor_adm.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.devgroup.professor_adm.dominio.Professor;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor,Integer> {
	
	@Transactional(readOnly = true)
	Professor findByEmail(String email);
	
}
