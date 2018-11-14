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
	public String criarCurso(@PathVariable("id") Integer id, Curso curso, BindingResult result, RedirectAttributes attr,
			ModelMap model) {

		Professor professor = repo.getOne(id);

		model.addAttribute("professor", professor);

		return "/professor/cursocadastro";
	}
	
	@GetMapping("/curso/excluir/{id}")
	public String excluiCurso(@PathVariable("id") Integer id, RedirectAttributes attr) {

           Curso curso = cursoDao.getOne(id);
			cursoDao.deleteById(id);
			
		return "redirect:/listarcurso/" + curso.getProfessor().getId();
	}

	@GetMapping("/listarcurso/{id}")
	public String listarCurso(@PathVariable("id") Integer id, Professor professor, BindingResult result,
			RedirectAttributes attr, ModelMap model) {

		Professor prof = repo.getOne(id);

		model.addAttribute("professor", prof);

		return "/professor/listarcursos";
	}
	
	@GetMapping("/editar/curso/{id}")
	public String editarCurso(@PathVariable("id") Integer id, Curso cur, BindingResult result,
			RedirectAttributes attr, ModelMap model) {

		Curso curso = cursoDao.getOne(id);

		model.addAttribute("curso",curso);

		return "/professor/cursoedicao";
	}


	@RequestMapping("/curso/salva")
	public String cursoSalvar(@Valid Curso curso, BindingResult result, RedirectAttributes attr, ModelMap model)
			throws DataIntegrityViolationException {

		Professor professor = repo.getOne(curso.getId());

		try {

			if (result.hasErrors()) {
				model.addAttribute("professor", professor);
				return "/professor/cursocadastro";
			}

			curso.setId(null);
			curso.setProfessor(professor);
			cursoDao.save(curso);
			attr.addFlashAttribute("message", "Curso cadastrado com sucesso!");

			return "redirect:/curso/edicao/" + professor.getId();
		} catch (DataIntegrityViolationException e) {
			attr.addFlashAttribute("messages", "O nome do curso já existe para este professor");

			return "redirect:/curso/edicao/" + professor.getId();
		}
	}

	@RequestMapping("/curso/salvaedicao")
	public String cursoEdita(@Valid Curso curso, BindingResult result, RedirectAttributes attr, ModelMap model)
			throws DataIntegrityViolationException {
		
		Curso cur = cursoDao.getOne(curso.getId());

		Professor professor = repo.getOne(cur.getProfessor().getId());

		try {

			if (result.hasErrors()) {
				model.addAttribute("curso", curso);
			     return "/professor/cursoedicao";
			}
			curso.setProfessor(professor);
			cursoDao.save(curso);
			attr.addFlashAttribute("message", "Curso editado com sucesso!");
			 return "redirect:/curso/edicao/" + professor.getId();

			
		} catch (DataIntegrityViolationException e) {
			attr.addFlashAttribute("messages", "O nome do curso já existe para este professor");
			model.addAttribute("curso", curso);
			 return "/professor/cursoedicao";	
		}
	}
}