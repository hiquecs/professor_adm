package com.devgroup.professor_adm.recursos;

import java.awt.image.BufferedImage;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.devgroup.professor_adm.Repositorios.ProfessorRepository;
import com.devgroup.professor_adm.dominio.Professor;
import com.devgroup.professor_adm.servicos.EmailService;
import com.devgroup.professor_adm.servicos.ImageService;
import com.devgroup.professor_adm.servicos.S3Service;

@Controller

public class LoginProfessorRecursos {

	@Autowired
	private ProfessorRepository dao;
	
	@Autowired
	private EmailService emailService;
	
	
	@Autowired
	private S3Service salva;
	
	@Autowired
	private ImageService imagem;
	
	@RequestMapping("/professor")
	public String index() {
		return "/professor/login_professor";
	}

	@RequestMapping("/professorsenhaemail")
	public String recuperaSenha() {
		return "/professor/recupera_senha";
	}

	@RequestMapping("/cadastrarprofessor")
	public String cadastrarProfessor(Professor professor) {
		return "/professor/cadastrar_professor";
	}

	@RequestMapping("/professor/cadastro")
	public String salvarProfessor( @RequestParam("file") MultipartFile file, @Valid Professor professor,
			BindingResult result, RedirectAttributes attr) {

		try {
			if (result.hasErrors()) {
				return "/professor/cadastrar_professor";
			}
			if(!file.isEmpty()) {
			
			  String a = ""+salva.uploadFile(file);
			  professor.setUrl(a);
			  
			}
			dao.save(professor);
			attr.addFlashAttribute("message", "Professor cadastrado com sucesso!");
			return "redirect:/cadastrarprofessor";
		} catch (Exception e) {
			attr.addFlashAttribute("messages", "Emails e RGMs já cadastrado no sistema não são permitidos");
			return "redirect:/cadastrarprofessor";
		}
	}

	@RequestMapping("/validacao/professor")
	public String preEditar(@Valid Professor professor, BindingResult result, RedirectAttributes attr, ModelMap model) {

		try {

			Professor pro = dao.findByEmail(professor.getEmail());

			if (pro.getSenha().equals(professor.getSenha())) {

				model.addAttribute("professor", pro);
				return "/professor/professor_principal";
			}

		} catch (Exception e) {

			attr.addFlashAttribute("messages", "Email ou Senha incorreto");
			return "redirect:/professor";
		}

		attr.addFlashAttribute("messages", "Email ou Senha incorreto");
		return "redirect:/professor";
	}

	@PostMapping("/professor/email/validacao")
	public String enviaSenhaRecuperada(@Valid Professor professor, BindingResult result, RedirectAttributes attr) {

		try {

			Professor pro = dao.findByEmail(professor.getEmail());

			emailService.sendPasswordEmail(pro);
			attr.addFlashAttribute("message", "Sua senha foi enviada ao email digitado com sucesso!");

			return "redirect:/professorsenhaemail";
		} catch (Exception e) {

			attr.addFlashAttribute("messages", "Email não encontrado no sistema");

			return "redirect:/professorsenhaemail";
		}
	}
}
