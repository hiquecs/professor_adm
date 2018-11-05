package com.devgroup.professor_adm.Repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devgroup.professor_adm.dominio.Materia;

@Repository
public interface MateriaRepository extends JpaRepository<Materia, Integer> {

}
