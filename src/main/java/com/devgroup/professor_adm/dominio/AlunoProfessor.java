package com.devgroup.professor_adm.dominio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class AlunoProfessor implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String nome;
	private String rgm;

	@OneToMany(mappedBy = "alunoProfessor")
	private List<AtividadeAluno> atividadeAluno = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "materia_id")
	private Materia materia;

	public AlunoProfessor() {

	}

	public AlunoProfessor(Integer id, String nome, String rgm, Materia materia) {

		this.id = id;
		this.nome = nome;
		this.rgm = rgm;
		this.materia = materia;
	}

	public List<AtividadeAluno> getAtividadeAluno() {
		return atividadeAluno;
	}

	public void setAtividadeAluno(List<AtividadeAluno> atividadeAluno) {
		this.atividadeAluno = atividadeAluno;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getRgm() {
		return rgm;
	}

	public void setRgm(String rgm) {
		this.rgm = rgm;
	}

	public Materia getMateria() {
		return materia;
	}

	public void setMateria(Materia materia) {
		this.materia = materia;
	}
}