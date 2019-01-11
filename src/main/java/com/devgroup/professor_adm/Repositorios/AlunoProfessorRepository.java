package com.devgroup.professor_adm.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devgroup.professor_adm.dominio.AlunoProfessor;

@Repository
public interface AlunoProfessorRepository extends JpaRepository<AlunoProfessor, Integer> {

}
