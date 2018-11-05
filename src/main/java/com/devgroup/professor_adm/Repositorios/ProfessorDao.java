package com.devgroup.professor_adm.Repositorios;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.devgroup.professor_adm.dominio.Professor;

@Repository
@Transactional
public class ProfessorDao {
	
	@Autowired
	private EntityManager entityManager;
	
	public void salvar(Professor professor) {
		entityManager.persist(professor);
	}
	
	public void atualizar(Professor professor) {
		entityManager.merge(professor);
	}
	
	public void delete(Integer id) {
		entityManager.remove( entityManager.getReference(Professor.class, id) );
	}
	
	public Professor buscarPorId(Integer id) {
		return entityManager.find(Professor.class, id);
	}
	
	public List<Professor> buscarTodos() {
		return entityManager
				.createQuery("select a from Professor a", Professor.class)
				.getResultList();				
	}
	
}







