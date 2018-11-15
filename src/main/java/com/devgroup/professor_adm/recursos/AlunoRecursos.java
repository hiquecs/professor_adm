package com.devgroup.professor_adm.recursos;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.devgroup.professor_adm.Repositorios.AlunoRepository;
import com.devgroup.professor_adm.dominio.Aluno;
import com.devgroup.professor_adm.servicos.S3Service;

@Controller
public class AlunoRecursos {

	@Autowired
	private AlunoRepository repo;

	@Autowired
	private S3Service salva;

	@GetMapping("aluno/editar/{id}")
	public String preEditar(@PathVariable("id") Integer id, ModelMap model) {

		Aluno aluno = repo.getOne(id);

		model.addAttribute("aluno", aluno);
		return "/aluno/editar_aluno";
	}

	@RequestMapping("/aluno/salvaredicao")
	public String editarAluno(@Valid Aluno aluno, BindingResult result, RedirectAttributes attr, ModelMap model) {

		try {

			if (result.hasErrors()) {

				attr.addFlashAttribute("messages", "Emails e RGMs já cadastrado no sistema não são permitidos");
				model.addAttribute("aluno", aluno);
				return "/aluno/editar_aluno";
			}

			repo.save(aluno);
			attr.addFlashAttribute("message", "Aluno atualizado com sucesso!");

			model.addAttribute("aluno", aluno);

			return "/aluno/aluno_principal";

		} catch (Exception e) {
			attr.addFlashAttribute("messages", "Emails e RGMs já cadastrado no sistema não são permitidos");
			model.addAttribute("aluno", aluno);
			return "/aluno/editar_aluno";
		}
	}

	@GetMapping("aluno/principal/{id}")
	public String telaPrincipal(@PathVariable("id") Integer id,Aluno aluno, ModelMap model) {

		Aluno alu = repo.getOne(id);

		model.addAttribute("aluno", alu);
		
		return "/aluno/aluno_principal";
	}

	@RequestMapping("/aluno/editarfoto")
	public String editarFoto(@RequestParam("file") MultipartFile file, @Valid Aluno aluno, RedirectAttributes attr,
			ModelMap model) {

		Aluno alu = repo.getOne(aluno.getId());
		try {
			String a = "" + salva.uploadFile(file);
			alu.setUrl(a);
			repo.save(alu);
			attr.addFlashAttribute("message", "Foto alterada com sucesso!");
			model.addAttribute("aluno", alu);
			return "/aluno/editar_aluno";
		} catch (Exception e) {
			attr.addFlashAttribute("messages", "Erro. foto nao alterada!");
			model.addAttribute("aluno", alu);
			return "/aluno/editar_aluno";
		}
	}
}