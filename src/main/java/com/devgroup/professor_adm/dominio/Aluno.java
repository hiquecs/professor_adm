package com.devgroup.professor_adm.dominio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;


@Entity
public class Aluno implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	
	@NotBlank(message="O Nome é um Campo obrigatório.")
	@Column(nullable=false)
	private String nome;
	

	@NotBlank(message="Email e um Campo obrigatorios")
	@Column(name = "emails",nullable=false,unique = true)
	private String email;
	
	@NotBlank(message="A Senha é um Campo obrigatório.")
	@Column(nullable=false)
	private String senha;
	
	@NotBlank(message="O RGM do aluno é obrigatório.")
	@Column(nullable=false,unique = true)
	private String rgm;

	private Integer grupo;
	
	@ManyToMany(mappedBy = "alunos")
	private List<Materia> materia = new ArrayList<>();
	
	public Aluno() {
	}

	public Aluno(Integer id, String nome, String email, String senha, String rgm, Integer grupo) {
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.rgm = rgm;
		this.grupo = grupo;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getRgm() {
		return rgm;
	}

	public void setRgm(String rgm) {
		this.rgm = rgm;
	}

	public Integer getGrupo() {
		return grupo;
	}

	public void setGrupo(Integer grupo) {
		this.grupo = grupo;
	}
	

	public List<Materia> getMaterias() {
		return materia;
	}

	public void setMaterias(List<Materia> materia) {
		this.materia = materia;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((grupo == null) ? 0 : grupo.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((rgm == null) ? 0 : rgm.hashCode());
		result = prime * result + ((senha == null) ? 0 : senha.hashCode());
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
		Aluno other = (Aluno) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (grupo == null) {
			if (other.grupo != null)
				return false;
		} else if (!grupo.equals(other.grupo))
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
		if (rgm == null) {
			if (other.rgm != null)
				return false;
		} else if (!rgm.equals(other.rgm))
			return false;
		if (senha == null) {
			if (other.senha != null)
				return false;
		} else if (!senha.equals(other.senha))
			return false;
		return true;
	}
}
