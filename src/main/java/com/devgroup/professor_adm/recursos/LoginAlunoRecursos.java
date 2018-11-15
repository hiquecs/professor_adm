package com.devgroup.professor_adm.recursos;

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
import com.devgroup.professor_adm.Repositorios.AlunoRepository;
import com.devgroup.professor_adm.dominio.Aluno;
import com.devgroup.professor_adm.servicos.EmailService;
import com.devgroup.professor_adm.servicos.S3Service;

@Controller

public class LoginAlunoRecursos {

	@Autowired 
	private AlunoRepository dao;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private S3Service salva;


	@RequestMapping("/aluno")
	public String index() {
		return "/aluno/login";
	}

	@RequestMapping("/senhaemail")
	public String recuperaSenha() {
		return "/aluno/recupera_senha";
	}

	@RequestMapping("/cadastraraluno")
	public String cadastrarAluno(Aluno aluno) {
		return "/aluno/cadastrar_aluno";
	}

	@RequestMapping("/aluno/cadastro")
	public String salvarAluno(@RequestParam("file") MultipartFile file,@Valid Aluno aluno, BindingResult result, RedirectAttributes attr) {

		try {
			if (result.hasErrors()) {
				return "/aluno/cadastrar_aluno";
			}
			if(!file.isEmpty()) {
				  String a = ""+salva.uploadFile(file);
				  aluno.setUrl(a);
				}
			dao.save(aluno);
			attr.addFlashAttribute("message", "Aluno cadastrado com sucesso!");
			return "redirect:/cadastraraluno";
		} catch (Exception e) {
			attr.addFlashAttribute("messages", "Emails e RGMs já cadastrado no sistema não são permitidos");
			return "redirect:/cadastraraluno";
		}
	}
	@RequestMapping("/validacao")
	public String preEditar(@Valid Aluno aluno, BindingResult result, RedirectAttributes attr,ModelMap model) {

		try {
			
		Aluno aluno1 = dao.findByEmail(aluno.getEmail());
		
		if (aluno1.getSenha().equals(aluno.getSenha())) {
			model.addAttribute("aluno", aluno1);
			
			return "/aluno/aluno_principal";
		} 
		
		}catch(Exception e) {
			
			attr.addFlashAttribute("messages", "Email ou Senha incorreto");
			return  "redirect:/aluno";
		}

		    attr.addFlashAttribute("messages", "Email ou Senha incorreto");
			return  "redirect:/aluno";
		}
	
	@PostMapping("/aluno/email/validacao")
	public String enviaSenhaRecuperada(@Valid Aluno aluno1, BindingResult result, RedirectAttributes attr) {

	    try {
	    	
	     	Aluno aluno = dao.findByEmail(aluno1.getEmail());
	     	
			emailService.sendPasswordEmail(aluno);
	     	attr.addFlashAttribute("message", "Sua senha foi enviada ao email digitado com sucesso!");
	     	
	     	return "redirect:/senhaemail";
	    	} catch (Exception e) {
			
			attr.addFlashAttribute("messages", "Email não encontrado no sistema");
			
			return "redirect:/senhaemail";
		}
	}
}
