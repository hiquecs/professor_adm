package com.devgroup.professor_adm.recursos;

import java.awt.image.BufferedImage;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
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
	private ImageService imageService;

	@GetMapping("/professor")
	public ModelAndView index(ModelAndView view) {
		view.setViewName("/professor/login_professor");

		return view;
	}

	@GetMapping("/professorsenhaemail")
	public ModelAndView recuperaSenha(ModelAndView view) {
		view.setViewName("/professor/recupera_senha");

		return view;
	}

	@GetMapping("/cadastrarprofessor")
	public ModelAndView cadastrarProfessor(Professor professor, ModelAndView view) {
		view.setViewName("/professor/cadastrar_professor");

		return view;
	}

	@PostMapping("/professor/cadastro")
	public ModelAndView salvarProfessor(@RequestParam("file") MultipartFile file, @Valid Professor professor,
			BindingResult result, RedirectAttributes attr, ModelAndView view) {

		try {
			if (result.hasErrors()) {
				view.setViewName("/professor/cadastrar_professor");

				return view;
			}
			if (!file.isEmpty()) {

				BufferedImage jpgImage = imageService.getJpgImageFromFile(file);
				jpgImage = imageService.cropSquare(jpgImage);
				jpgImage = imageService.resize(jpgImage, 200);
				String fileName = professor.getEmail() + ".jpg";
				String a = "" + salva.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");
				professor.setUrl(a);

			}
			dao.save(professor);
			attr.addFlashAttribute("message", "Professor cadastrado com sucesso!");
			view.setViewName("redirect:/cadastrarprofessor");

			return view;
		} catch (Exception e) {
			attr.addFlashAttribute("messages", "Emails e RGMs já cadastrado no sistema não são permitidos");
			view.setViewName("redirect:/cadastrarprofessor");

			return view;
		}
	}

	@PostMapping("/validacao/professor")
	public ModelAndView preEditar(Professor professor, RedirectAttributes attr, ModelAndView view) {

		try {

			Professor pro = dao.findByEmail(professor.getEmail());

			if (pro.getSenha().equals(professor.getSenha())) {

				view.addObject("professor", pro);
				view.setViewName("/professor/professor_principal");

				return view;
			}

		} catch (Exception e) {

			attr.addFlashAttribute("messages", "Email ou Senha incorreto");
			view.setViewName("redirect:/professor");

			return view;
		}

		attr.addFlashAttribute("messages", "Email ou Senha incorreto");
		view.setViewName("redirect:/professor");

		return view;
	}

	@PostMapping("/professor/email/validacao")
	public ModelAndView enviaSenhaRecuperada(@Valid Professor professor, RedirectAttributes attr, ModelAndView view) {

		try {

			Professor pro = dao.findByEmail(professor.getEmail());

			emailService.sendPasswordEmail(pro);
			attr.addFlashAttribute("message", "Sua senha foi enviada ao email digitado com sucesso!");
			view.setViewName("redirect:/professorsenhaemail");

			return view;
		} catch (Exception e) {

			attr.addFlashAttribute("messages", "Email não encontrado no sistema");
			view.setViewName("redirect:/professorsenhaemail");

			return view;
		}
	}
}