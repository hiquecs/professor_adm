package com.devgroup.professor_adm.recursos;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.devgroup.professor_adm.Repositorios.CursoRepository;
import com.devgroup.professor_adm.Repositorios.MateriaRepository;
import com.devgroup.professor_adm.Repositorios.ProfessorRepository;
import com.devgroup.professor_adm.dominio.Curso;
import com.devgroup.professor_adm.dominio.Materia;
import com.devgroup.professor_adm.dominio.Professor;

@Controller
public class MateriaRecursos {

	@Autowired
	private ProfessorRepository repo;

	@Autowired
	private CursoRepository repoCurso;

	@Autowired
	private MateriaRepository materiaRepo;

	@GetMapping("/prepara/cadastrodisciplina/{id}")
	public String selecionaCurso(@PathVariable("id") Integer id, ModelMap model) {

		Professor professor = repo.getOne(id);

		model.addAttribute("professor", professor);

		return "/professor/curso_disciplina_lista";

	}

	@GetMapping("/editar/materia/{id}")
	public String editarCurso(@PathVariable("id") Integer id, Materia materia, BindingResult result,
			RedirectAttributes attr, ModelMap model) {

		Curso curso = repoCurso.getOne(id);
		model.addAttribute("curso", curso);

		return "/professor/materiacadastro";
	}

	@GetMapping("/materia/excluir/{id}")
	public String excluiMateria(@PathVariable("id") Integer id, RedirectAttributes attr) {

		Materia materia = materiaRepo.getOne(id);
		materiaRepo.deleteById(id);

		return "redirect:/listarmaterias/" + materia.getCurso().getId();
	}

	@GetMapping("/editar/materia_ediçao/{id}")
	public String salvarEditaçãoCurso(@PathVariable("id") Integer id, Materia materia, BindingResult result,
			RedirectAttributes attr, ModelMap model) {

		Materia mat = materiaRepo.getOne(id);

		Professor professor = repo.getOne(mat.getCurso().getProfessor().getId());

		model.addAttribute("materia", mat);
		model.addAttribute("professor", professor);

		return "/professor/materiaedicao";
	}

	@GetMapping("/listarmaterias/{id}")
	public String listarMaterias(@PathVariable("id") Integer id, ModelMap model) {

		Curso curso = repoCurso.getOne(id);

		Professor professor = repo.getOne(curso.getProfessor().getId());

		model.addAttribute("curso", curso);
		model.addAttribute("professor", professor);

		return "/professor/listarmaterias";
	}

	@GetMapping("/cadastra/cadastrodisciplina/{id}")
	public String formDisciplina(@PathVariable("id") Integer id, Materia materia, ModelMap model) {

		Curso curso = repoCurso.getOne(id);

		model.addAttribute("curso", curso);

		return "/professor/materiacadastro";
	}

	@RequestMapping("/materia/salva")
	public String materiaSalvar(@Valid Materia materia, BindingResult result, RedirectAttributes attr, ModelMap model)
			throws DataIntegrityViolationException {

		Curso curso = repoCurso.getOne(materia.getId());

		try {

			if (result.hasErrors()) {
				model.addAttribute("curso", curso);
				return "/professor/materiacadastro";
			}

			Materia mat = new Materia(null, materia.getNome(), materia.getQuantidadeAlunos(), 0.0f, false, curso);

			materiaRepo.save(mat);
			attr.addFlashAttribute("message", "Disciplina cadastrada com sucesso!");

			return "redirect:/cadastra/cadastrodisciplina/" + curso.getId();
		} catch (DataIntegrityViolationException e) {
			attr.addFlashAttribute("messages", "O nome da Disciplina já existe para este Curso");

			return "redirect:/cadastra/cadastrodisciplina/" + curso.getId();
		}
	}

	@RequestMapping("/materia/salvaedicao")
	public String materiaSalvarEdicao(@Valid Materia materia, BindingResult result, RedirectAttributes attr,
			ModelMap model) throws DataIntegrityViolationException {

		Materia mat = materiaRepo.getOne(materia.getId());
		mat.setNome(materia.getNome());
		mat.setQuantidadeAlunos(materia.getQuantidadeAlunos());

		Curso curso = repoCurso.getOne(mat.getCurso().getId());

		try {

			if (result.hasErrors()) {
				model.addAttribute("materia", materia);
				return "/professor/materiaedicao";
			}

			materiaRepo.save(mat);
			attr.addFlashAttribute("message", "Disciplina editada com sucesso!");

			return "redirect:/cadastra/cadastrodisciplina/" + curso.getId();
		} catch (DataIntegrityViolationException e) {
			attr.addFlashAttribute("messages", "O nome da Disciplina já existe para este Curso");

			model.addAttribute("materia", materia);
			return "/professor/materiaedicao";
		}
	}

}
