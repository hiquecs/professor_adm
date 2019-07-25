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
import com.devgroup.professor_adm.Repositorios.ProfessorRepository;
import com.devgroup.professor_adm.dominio.Curso;
import com.devgroup.professor_adm.dominio.Professor;

@Controller
public class CursoRecursos {

	@Autowired
	private ProfessorRepository repo;

	@Autowired
	private CursoRepository cursoDao;

	@GetMapping("/curso/edicao/{id}")
	public ModelAndView criarCurso(@PathVariable("id") Integer id, Curso curso, ModelAndView view) {

		Professor professor = repo.getOne(id);

		view.addObject("professor", professor);
		view.addObject("curso", curso);
		view.setViewName("/professor/cursocadastro");

		return view;
	}

	@GetMapping("/curso/excluir/{id}")
	public ModelAndView excluiCurso(@PathVariable("id") Integer id, ModelAndView view) {

		Curso curso = cursoDao.getOne(id);
		cursoDao.deleteById(id);

		view.setViewName("redirect:/listarcurso/" + curso.getProfessor().getId());

		return view;
	}

	@GetMapping("/listarcurso/{id}")
	public ModelAndView listarCurso(@PathVariable("id") Integer id, ModelAndView view) {

		Professor professor = repo.getOne(id);

		view.addObject("professor", professor);
		view.setViewName("/professor/listarcursos");

		return view;
	}

	@GetMapping("/editar/curso/{id}")
	public ModelAndView editarCurso(@PathVariable("id") Integer id, ModelAndView view) {

		Curso curso = cursoDao.getOne(id);

		view.addObject("curso", curso);
		view.setViewName("/professor/cursoedicao");

		return view;
	}

	@PostMapping("/curso/salva")
	public ModelAndView cursoSalvar(@Valid Curso curso, BindingResult result, RedirectAttributes attr,
			ModelAndView view) throws DataIntegrityViolationException {

		Professor professor = repo.getOne(curso.getId());

		try {

			if (result.hasErrors()) {
				view.addObject("professor", professor);
				view.setViewName("/professor/cursocadastro");

				return view;
			}

			curso.setId(null);
			curso.setProfessor(professor);
			cursoDao.save(curso);
			attr.addFlashAttribute("message", "Curso cadastrado com sucesso!");
			view.setViewName("redirect:/curso/edicao/" + professor.getId());

			return view;
		} catch (DataIntegrityViolationException e) {
			attr.addFlashAttribute("messages", "O nome do curso já existe para este professor");
			view.setViewName("redirect:/curso/edicao/" + professor.getId());

			return view;
		}
	}

	@PostMapping("/curso/salvaedicao")
	public ModelAndView cursoEdita(@Valid Curso curso, BindingResult result, RedirectAttributes attr, ModelAndView view)
			throws DataIntegrityViolationException {

		Curso cur = cursoDao.getOne(curso.getId());

		Professor professor = repo.getOne(cur.getProfessor().getId());

		try {

			if (result.hasErrors()) {
				view.addObject("curso", curso);
				view.setViewName("/professor/cursoedicao");

				return view;
			}
			curso.setProfessor(professor);
			cursoDao.save(curso);
			attr.addFlashAttribute("message", "Curso editado com sucesso!");
			view.setViewName("redirect:/curso/edicao/" + professor.getId());

			return view;

		} catch (DataIntegrityViolationException e) {
			attr.addFlashAttribute("messages", "O nome do curso já existe para este professor");
			view.addObject("curso", curso);
			view.setViewName("/professor/cursoedicao");

			return view;
		}
	}
}