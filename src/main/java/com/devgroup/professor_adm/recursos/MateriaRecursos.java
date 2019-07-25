package com.devgroup.professor_adm.recursos;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
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
	public ModelAndView selecionaCurso(@PathVariable("id") Integer id, ModelAndView view) {

		Professor professor = repo.getOne(id);

		view.addObject("professor", professor);
		view.setViewName("/professor/curso_disciplina_lista");

		return view;

	}

	@GetMapping("/editar/materia/{id}")
	public ModelAndView editarCurso(@PathVariable("id") Integer id, Materia materia, ModelAndView view) {

		Curso curso = repoCurso.getOne(id);
		view.addObject("curso", curso);
		view.addObject("materia", materia);
		view.setViewName("/professor/materiacadastro");

		return view;
	}

	@GetMapping("/materia/excluir/{id}")
	public ModelAndView excluiMateria(@PathVariable("id") Integer id, ModelAndView view) {

		Materia materia = materiaRepo.getOne(id);
		materiaRepo.deleteById(id);
		view.setViewName("redirect:/listarmaterias/" + materia.getCurso().getId());

		return view;
	}

	@GetMapping("/editar/materia_ediçao/{id}")
	public ModelAndView salvarEditaçãoCurso(@PathVariable("id") Integer id, ModelAndView view) {

		Materia mat = materiaRepo.getOne(id);

		Professor professor = repo.getOne(mat.getCurso().getProfessor().getId());

		view.addObject("materia", mat);
		view.addObject("professor", professor);
		view.setViewName("/professor/materiaedicao");

		return view;
	}

	@GetMapping("/listarmaterias/{id}")
	public ModelAndView listarMaterias(@PathVariable("id") Integer id, ModelAndView view) {

		Curso curso = repoCurso.getOne(id);

		Professor professor = repo.getOne(curso.getProfessor().getId());

		view.addObject("curso", curso);
		view.addObject("professor", professor);
		view.setViewName("/professor/listarmaterias");

		return view;
	}

	@GetMapping("/cadastra/cadastrodisciplina/{id}")
	public ModelAndView formDisciplina(@PathVariable("id") Integer id, Materia materia, ModelAndView view) {

		Curso curso = repoCurso.getOne(id);

		view.addObject("curso", curso);
		view.addObject("materia", materia);
		view.setViewName("/professor/materiacadastro");

		return view;
	}

	@PostMapping("/materia/salva")
	public ModelAndView materiaSalvar(@Valid Materia materia, BindingResult result, RedirectAttributes attr,
			ModelAndView view) throws DataIntegrityViolationException {

		Curso curso = repoCurso.getOne(materia.getId());

		try {

			if (result.hasErrors()) {
				view.addObject("curso", curso);
				view.setViewName("/professor/materiacadastro");

				return view;
			}

			Materia mat = new Materia(null, materia.getNome(), materia.getQuantidadeAlunos(), 0.0f, false, curso);

			materiaRepo.save(mat);
			attr.addFlashAttribute("message", "Disciplina cadastrada com sucesso!");
			view.setViewName("redirect:/cadastra/cadastrodisciplina/" + curso.getId());

			return view;
		} catch (DataIntegrityViolationException e) {
			attr.addFlashAttribute("messages", "O nome da Disciplina já existe para este Curso");
			view.setViewName("redirect:/cadastra/cadastrodisciplina/" + curso.getId());

			return view;
		}
	}

	@PostMapping("/materia/salvaedicao")
	public ModelAndView materiaSalvarEdicao(@Valid Materia materia, BindingResult result, RedirectAttributes attr,
			ModelAndView view) throws DataIntegrityViolationException {

		Materia mat = materiaRepo.getOne(materia.getId());
		mat.setNome(materia.getNome());
		mat.setQuantidadeAlunos(materia.getQuantidadeAlunos());

		Curso curso = repoCurso.getOne(mat.getCurso().getId());

		try {

			if (result.hasErrors()) {
				view.addObject("materia", materia);
				view.setViewName("/professor/materiaedicao");

				return view;
			}

			materiaRepo.save(mat);
			attr.addFlashAttribute("message", "Disciplina editada com sucesso!");
			view.setViewName("redirect:/cadastra/cadastrodisciplina/" + curso.getId());

			return view;
		} catch (DataIntegrityViolationException e) {
			attr.addFlashAttribute("messages", "O nome da Disciplina já existe para este Curso");

			view.addObject("materia", materia);
			view.setViewName("/professor/materiaedicao");
			return view;
		}
	}
}