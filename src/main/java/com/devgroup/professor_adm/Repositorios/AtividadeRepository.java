package com.devgroup.professor_adm.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devgroup.professor_adm.dominio.Atividade;

@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, Integer> {



}
