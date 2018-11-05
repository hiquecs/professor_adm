package com.devgroup.professor_adm.servicos;

import org.springframework.mail.SimpleMailMessage;

import com.devgroup.professor_adm.dominio.Aluno;
import com.devgroup.professor_adm.dominio.Professor;

public interface EmailService {

	void sendEmail(SimpleMailMessage msg);
	
	void sendPasswordEmail(Aluno aluno);
	void sendPasswordEmail(Professor professor);
}