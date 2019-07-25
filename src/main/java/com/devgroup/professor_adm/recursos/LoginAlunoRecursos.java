package com.devgroup.professor_adm.recursos;

import java.awt.image.BufferedImage;
import java.util.List;
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
import com.devgroup.professor_adm.Repositorios.AlunoRepository;
import com.devgroup.professor_adm.dominio.Aluno;
import com.devgroup.professor_adm.dominio.Materia;
import com.devgroup.professor_adm.servicos.EmailService;
import com.devgroup.professor_adm.servicos.ImageService;
import com.devgroup.professor_adm.servicos.S3Service;

@Controller
public class LoginAlunoRecursos {

	@Autowired
	private AlunoRepository dao;

	@Autowired
	private EmailService emailService;

	@Autowired
	private ImageService imageService;

	@Autowired
	private S3Service salva;

	@GetMapping("/aluno")
	public ModelAndView index(ModelAndView view) {
		view.setViewName("/aluno/login");

		return view;
	}

	@GetMapping("/senhaemail")
	public String recuperaSenha() {
		return "/aluno/recupera_senha";
	}

	@GetMapping("/cadastraraluno")
	public ModelAndView cadastrarAluno(Aluno aluno, ModelAndView view) {

		view.addObject("aluno", aluno);
		view.setViewName("/aluno/cadastrar_aluno");

		return view;
	}

	@PostMapping("/aluno/cadastro")
	public ModelAndView salvarAluno(@RequestParam("file") MultipartFile file, @Valid Aluno aluno, BindingResult result,
			RedirectAttributes attr, ModelAndView view) {

		try {
			if (result.hasErrors()) {
				view.setViewName("/aluno/cadastrar_aluno");

				return view;
			}
			if (!file.isEmpty()) {

				BufferedImage jpgImage = imageService.getJpgImageFromFile(file);
				jpgImage = imageService.cropSquare(jpgImage);
				jpgImage = imageService.resize(jpgImage, 200);
				String fileName = aluno.getRgm() + ".jpg";
				String a = "" + salva.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");
				aluno.setUrl(a);
			}
			dao.save(aluno);
			attr.addFlashAttribute("message", "Aluno cadastrado com sucesso!");
			view.setViewName("redirect:/cadastraraluno");

			return view;
		} catch (Exception e) {
			attr.addFlashAttribute("messages", "Emails e RGMs já cadastrado no sistema não são permitidos");
			view.setViewName("redirect:/cadastraraluno");

			return view;
		}
	}

	@PostMapping("/validacao")
	public ModelAndView preEditar(Aluno aluno, RedirectAttributes attr, ModelAndView view) {

		try {

			Aluno aluno1 = dao.findByEmail(aluno.getEmail());

			if (aluno1.getSenha().equals(aluno.getSenha())) {

				List<Materia> materia = aluno1.getMateria();

				view.addObject("materia", materia);
				view.addObject("aluno", aluno1);
				view.setViewName("/aluno/aluno_principal");

				return view;
			}

		} catch (Exception e) {

			attr.addFlashAttribute("messages", "Email ou Senha incorreto");
			view.setViewName("redirect:/aluno");

			return view;
		}

		attr.addFlashAttribute("messages", "Email ou Senha incorreto");
		view.setViewName("redirect:/aluno");

		return view;
	}

	@PostMapping("/aluno/email/validacao")
	public ModelAndView enviaSenhaRecuperada(@Valid Aluno aluno1, RedirectAttributes attr, ModelAndView view) {

		try {

			Aluno aluno = dao.findByEmail(aluno1.getEmail());

			emailService.sendPasswordEmail(aluno);
			attr.addFlashAttribute("message", "Sua senha foi enviada ao email digitado com sucesso!");
			view.setViewName("redirect:/senhaemail");

			return view;
		} catch (Exception e) {

			attr.addFlashAttribute("messages", "Email não encontrado no sistema");
			view.setViewName("redirect:/senhaemail");

			return view;
		}
	}
}