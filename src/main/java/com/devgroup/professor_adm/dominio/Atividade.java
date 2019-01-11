package com.devgroup.professor_adm.dominio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Atividade implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank(message = "O nome da atividade é um Campo obrigatório.")
	@Column(nullable = false)
	private String nome;
	private String anotacoes;
	private Integer quantidadeAlunosGrupo;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dataCriacao;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dataEntrega;

	private String url;

	@ManyToOne
	@JoinColumn(name = "materia_id")
	private Materia materia;
	
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "atividade")
	private List<AtividadeAluno> atividadeAluno = new ArrayList<>();

	public Atividade() {

		quantidadeAlunosGrupo = 0;

	}

	public Atividade(Integer id, @NotBlank(message = "O nome da atividade é um Campo obrigatório.") String nome,
			String anotacoes, Integer quantidadeAlunosGrupo, Date dataCriacao, Date dataEntrega, String url,
			Materia materia) {
	
		this.id = id;
		this.nome = nome;
		this.anotacoes = anotacoes;
		this.quantidadeAlunosGrupo = quantidadeAlunosGrupo;
		this.dataCriacao = dataCriacao;
		this.dataEntrega = dataEntrega;
		this.url = url;
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

	public String getAnotacoes() {
		return anotacoes;
	}

	public void setAnotacoes(String anotacoes) {
		this.anotacoes = anotacoes;
	}

	public Integer getQuantidadeAlunosGrupo() {
		return quantidadeAlunosGrupo;
	}

	public void setQuantidadeAlunosGrupo(Integer quantidadeAlunosGrupo) {
		this.quantidadeAlunosGrupo = quantidadeAlunosGrupo;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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
		result = prime * result + ((materia == null) ? 0 : materia.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((quantidadeAlunosGrupo == null) ? 0 : quantidadeAlunosGrupo.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
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
		if (materia == null) {
			if (other.materia != null)
				return false;
		} else if (!materia.equals(other.materia))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (quantidadeAlunosGrupo == null) {
			if (other.quantidadeAlunosGrupo != null)
				return false;
		} else if (!quantidadeAlunosGrupo.equals(other.quantidadeAlunosGrupo))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
}