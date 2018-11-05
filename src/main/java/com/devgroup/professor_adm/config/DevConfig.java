package com.devgroup.professor_adm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.devgroup.professor_adm.servicos.EmailService;
import com.devgroup.professor_adm.servicos.SmtpEmailService;

@Configuration
public class DevConfig {


	@Bean
	public EmailService emailService() {
		return new SmtpEmailService();
	}
}