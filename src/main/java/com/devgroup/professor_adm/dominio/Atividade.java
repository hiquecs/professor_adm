package com.devgroup.professor_adm.dominio;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Atividade implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String nome;
	private Float nota;
	private String nomeGrupo;
	private String anotacoes;
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private Date dataCriacao;
	@JsonFormat(pattern = "dd/MM/yyyy HH:mm")
	private Date dataEntrega;

	@ManyToOne
	@JoinColumn(name = "materia_id")
	private Materia materia;

	public Atividade() {

	}



	public Atividade(Integer id, String nome, Float nota, String nomeGrupo, String anotacoes, Date dataCriacao,
			Date dataEntrega, Materia materia) {
		this.id = id;
		this.nome = nome;
		this.nota = nota;
		this.nomeGrupo = nomeGrupo;
		this.anotacoes = anotacoes;
		this.dataCriacao = dataCriacao;
		this.dataEntrega = dataEntrega;
		this.materia = materia;
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



	public Float getNota() {
		return nota;
	}



	public void setNota(Float nota) {
		this.nota = nota;
	}



	public String getNomeGrupo() {
		return nomeGrupo;
	}



	public void setNomeGrupo(String nomeGrupo) {
		this.nomeGrupo = nomeGrupo;
	}



	public String getAnotacoes() {
		return anotacoes;
	}



	public void setAnotacoes(String anotacoes) {
		this.anotacoes = anotacoes;
	}



	public Date getDataCriacao() {
		return dataCriacao;
	}



	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}



	public Date getDataEntrega() {
		return dataEntrega;
	}



	public void setDataEntrega(Date dataEntrega) {
		this.dataEntrega = dataEntrega;
	}



	public Materia getMateria() {
		return materia;
	}



	public void setMateria(Materia materia) {
		this.materia = materia;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((anotacoes == null) ? 0 : anotacoes.hashCode());
		result = prime * result + ((dataCriacao == null) ? 0 : dataCriacao.hashCode());
		result = prime * result + ((dataEntrega == null) ? 0 : dataEntrega.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((nomeGrupo == null) ? 0 : nomeGrupo.hashCode());
		result = prime * result + ((nota == null) ? 0 : nota.hashCode());
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
		Atividade other = (Atividade) obj;
		if (anotacoes == null) {
			if (other.anotacoes != null)
				return false;
		} else if (!anotacoes.equals(other.anotacoes))
			return false;
		if (dataCriacao == null) {
			if (other.dataCriacao != null)
				return false;
		} else if (!dataCriacao.equals(other.dataCriacao))
			return false;
		if (dataEntrega == null) {
			if (other.dataEntrega != null)
				return false;
		} else if (!dataEntrega.equals(other.dataEntrega))
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
		if (nomeGrupo == null) {
			if (other.nomeGrupo != null)
				return false;
		} else if (!nomeGrupo.equals(other.nomeGrupo))
			return false;
		if (nota == null) {
			if (other.nota != null)
				return false;
		} else if (!nota.equals(other.nota))
			return false;
		return true;
	}
}