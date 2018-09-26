package com.devgroup.professor_adm.dominio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Materia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String nome;
	private Integer quantidadeAlunos;
	private Float notaTotal;

	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private Date dataCriacao;

	@ManyToOne
	@JoinColumn(name = "curso_id")
	private Curso curso;
	
	@JsonIgnore
	@OneToMany(mappedBy = "materia")
	private List<Atividade> atividade = new ArrayList<>();

	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "materia_aluno", joinColumns = @JoinColumn(name = "materia_id"), inverseJoinColumns = @JoinColumn(name = "aluno_id"))
	private List<Aluno> alunos = new ArrayList<>();

	public Materia() {
	}

	public Materia(Integer id, String nome, Integer quantidadeAlunos, Float notaTotal, Date dataCriacao, Curso curso) {
		super();
		this.id = id;
		this.nome = nome;
		this.quantidadeAlunos = quantidadeAlunos;
		this.notaTotal = notaTotal;
		this.dataCriacao = dataCriacao;
		this.curso = curso;
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

	public Integer getQuantidadeAlunos() {
		return quantidadeAlunos;
	}

	public void setQuantidadeAlunos(Integer quantidadeAlunos) {
		this.quantidadeAlunos = quantidadeAlunos;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public List<Aluno> getCategorias() {
		return alunos;
	}

	public void setCategorias(List<Aluno> categorias) {
		this.alunos = categorias;
	}

	public Float getNotaTotal() {
		return notaTotal;
	}

	public void setNotaTotal(Float notaTotal) {
		this.notaTotal = notaTotal;
	}

	public List<Aluno> getAlunos() {
		return alunos;
	}

	public void setAlunos(List<Aluno> alunos) {
		this.alunos = alunos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alunos == null) ? 0 : alunos.hashCode());
		result = prime * result + ((curso == null) ? 0 : curso.hashCode());
		result = prime * result + ((dataCriacao == null) ? 0 : dataCriacao.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((notaTotal == null) ? 0 : notaTotal.hashCode());
		result = prime * result + ((quantidadeAlunos == null) ? 0 : quantidadeAlunos.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Materia other = (Materia) obj;
		if (alunos == null) {
			if (other.alunos != null)
				return false;
		} else if (!alunos.equals(other.alunos))
			return false;
		if (curso == null) {
			if (other.curso != null)
				return false;
		} else if (!curso.equals(other.curso))
			return false;
		if (dataCriacao == null) {
			if (other.dataCriacao != null)
				return false;
		} else if (!dataCriacao.equals(other.dataCriacao))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (notaTotal == null) {
			if (other.notaTotal != null)
				return false;
		} else if (!notaTotal.equals(other.notaTotal))
			return false;
		if (quantidadeAlunos == null) {
			if (other.quantidadeAlunos != null)
				return false;
		} else if (!quantidadeAlunos.equals(other.quantidadeAlunos))
			return false;
		return true;
	}

}