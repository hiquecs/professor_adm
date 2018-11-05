package com.devgroup.professor_adm.servicos;

import java.util.Date;

import org.springframework.mail.SimpleMailMessage;

import com.devgroup.professor_adm.dominio.Aluno;
import com.devgroup.professor_adm.dominio.Professor;

public abstract class AbstractEmailService implements EmailService {

	@Override
	public void sendPasswordEmail(Professor professor) {
		SimpleMailMessage sm = preparePasswordEmail(professor);
		sendEmail(sm);
	}
	protected SimpleMailMessage preparePasswordEmail(Professor professor) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(professor.getEmail());
		sm.setFrom("deliverysenhaprofessoradm@gmail.com");
		sm.setSubject("Solicitação de senha extraviada");
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText(professor.getNome()+" \nSua senha foi recuperada com sucesso !!"
				+ "\nSenha recuperda: "+ professor.getSenha());
		return sm;
	}


	@Override
	public void sendPasswordEmail(Aluno aluno) {
		SimpleMailMessage sm = preparePasswordEmail(aluno);
		sendEmail(sm);

	}

	protected SimpleMailMessage preparePasswordEmail(Aluno aluno) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(aluno.getEmail());
		sm.setFrom("deliverysenhaprofessoradm@gmail.com");
		sm.setSubject("Solicitação de senha extraviada");
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText(aluno.getNome()+" \nSua senha foi recuperada com sucesso !!"
				+ "\nSenha recuperda: "+ aluno.getSenha());
		return sm;
	}
}
