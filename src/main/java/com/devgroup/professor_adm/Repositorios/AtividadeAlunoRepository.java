package com.devgroup.professor_adm.Repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.devgroup.professor_adm.dominio.AtividadeAluno;


@Repository
public interface AtividadeAlunoRepository extends JpaRepository<AtividadeAluno, Integer> {

	@Query("Select obj FROM AtividadeAluno obj Where obj.materia.id = :materiaId and  obj.atividade.id = :atividadeId ORDER BY obj.grupo")
	public List<AtividadeAluno> findGrupos(@Param("materiaId") Integer materia_id,@Param("atividadeId") Integer atividade_id);
	
	@Query("Select obj FROM AtividadeAluno obj Where obj.materia.id = :materiaId and  obj.aluno.id = :alunoId")
	public  List<AtividadeAluno> findAlunoAtividades(@Param("materiaId") Integer materia_id , @Param("alunoId") Integer aluno_id);
	
	@Query("Select obj FROM AtividadeAluno obj Where obj.materia.id = :materiaId and  obj.aluno.id = :alunoId  and  obj.atividade.id = :atividadeId")
	public  AtividadeAluno findAtividadeAluno(@Param("materiaId") Integer materia_id , @Param("alunoId") Integer aluno_id,@Param("atividadeId") Integer atividade_id);
	
	@Query("Select obj FROM AtividadeAluno obj Where obj.atividade.id = :atividadeId and  obj.grupo = :nomeGrupo")
	public List<AtividadeAluno> findAtividadePorNomeGrupo(@Param("atividadeId") Integer atividade_id , @Param("nomeGrupo") String grupo_nome);
	
	@Query("Select obj FROM AtividadeAluno obj Where obj.materia.id = :materiaId  ORDER BY obj.aluno.id DESC , obj.atividade.nome ASC")
	public List<AtividadeAluno> findAtividades(@Param("materiaId") Integer materia_id);
}

