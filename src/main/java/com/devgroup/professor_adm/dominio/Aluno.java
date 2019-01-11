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
import javax.persistence.OneToMany;
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
	
	@ManyToMany(mappedBy = "alunos")
	private List<Materia> materia = new ArrayList<>();
    
	private Integer grupo;
	
	private String url;

	@OneToMany(mappedBy = "aluno")
	private List<AtividadeAluno> atividadeAluno = new ArrayList<>();

	public Aluno() {
		
		url = "https://professor-adm.s3.sa-east-1.amazonaws.com/avatar-blank.png";
	}

	

	public Aluno(Integer id, @NotBlank(message = "O Nome é um Campo obrigatório.") String nome,
			@NotBlank(message = "Email e um Campo obrigatorios") String email,
			@NotBlank(message = "A Senha é um Campo obrigatório.") String senha,
			@NotBlank(message = "O RGM do aluno é obrigatório.") String rgm, Integer grupo, String url) {
		
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.rgm = rgm;
		this.grupo = grupo;
		this.url = url;
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

	public List<Materia> getMateria() {
		return materia;
	}

	public void setMateria(List<Materia> materia) {
		this.materia = materia;
	}

	public Integer getGrupo() {
		return grupo;
	}

	public void setGrupo(Integer grupo) {
		this.grupo = grupo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<AtividadeAluno> getAtividadeAluno() {
		return atividadeAluno;
	}

	public void setAtividadeAluno(List<AtividadeAluno> atividadeAluno) {
		this.atividadeAluno = atividadeAluno;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((atividadeAluno == null) ? 0 : atividadeAluno.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((grupo == null) ? 0 : grupo.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((materia == null) ? 0 : materia.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((rgm == null) ? 0 : rgm.hashCode());
		result = prime * result + ((senha == null) ? 0 : senha.hashCode());
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
		Aluno other = (Aluno) obj;
		if (atividadeAluno == null) {
			if (other.atividadeAluno != null)
				return false;
		} else if (!atividadeAluno.equals(other.atividadeAluno))
			return false;
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
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}	
}