package com.devgroup.professor_adm.Repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.devgroup.professor_adm.dominio.Atividade;

@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, Integer> {
	
	@Query("Select obj FROM Atividade obj Where obj.materia.id = :materiaId AND obj.quantidadeAlunosGrupo > 1")
	public List<Atividade> findAtividadeComGrupo(@Param("materiaId") Integer materia_id);
}
