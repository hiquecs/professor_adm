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

import com.devgroup.professor_adm.Repositorios.AtividadeRepository;
import com.devgroup.professor_adm.Repositorios.CursoRepository;
import com.devgroup.professor_adm.Repositorios.MateriaRepository;
import com.devgroup.professor_adm.Repositorios.ProfessorRepository;
import com.devgroup.professor_adm.dominio.Atividade;
import com.devgroup.professor_adm.dominio.Curso;
import com.devgroup.professor_adm.dominio.Materia;
import com.devgroup.professor_adm.dominio.Professor;


@Controller
public class AtividadeRecursos {

	@Autowired
	private ProfessorRepository repo;

	@Autowired
	private CursoRepository cursoRepo;

	@Autowired
	private MateriaRepository materiaRepo;

	@Autowired
	private AtividadeRepository atividadeRepo;

	@GetMapping("/escolhecurso/{id}")
	public String selecionaCurso(@PathVariable("id") Integer id, ModelMap model) {

		Professor professor = repo.getOne(id);

		model.addAttribute("professor", professor);

		return "/professor/listarcursosatividades";

	}

	@GetMapping("/escolhemateria/{id}")
	public String selecionaMateria(@PathVariable("id") Integer id, ModelMap model) {

		Curso curso = cursoRepo.getOne(id);

		Professor professor = repo.getOne(curso.getProfessor().getId());

		model.addAttribute("professor", professor);
		model.addAttribute("curso", curso);

		return "/professor/listarmateriasatividades";

	}
	
	@GetMapping("/listaratividades/{id}")
	public String selecionaAtividades(@PathVariable("id") Integer id, ModelMap model) {

		Materia materia = materiaRepo.getOne(id);

		Professor professor = repo.getOne(materia.getCurso().getProfessor().getId());

		model.addAttribute("professor", professor);
		model.addAttribute("materia", materia);

		return "/professor/listaratividades";

	}
	
	@GetMapping("/editar/atividade/{id}")
	public String selecionaAtividade(@PathVariable("id") Integer id, ModelMap model) {
		
		Atividade atividade = atividadeRepo.getOne(id);
		Materia materia = materiaRepo.getOne(atividade.getMateria().getId());
		Curso curso = cursoRepo.getOne(materia.getCurso().getId());
		Professor professor = repo.getOne(curso.getProfessor().getId());

		model.addAttribute("professor", professor);
		model.addAttribute("atividade", atividade);

		return "/professor/atividadeedicao";

	}

	@GetMapping("/preparacadastroatividade/{id}")
	public String preparaCadastroAtividade(@PathVariable("id") Integer id, Atividade atividade, ModelMap model) {

		Materia materia = materiaRepo.getOne(id);
		Professor professor = repo.getOne(materia.getCurso().getProfessor().getId());

		model.addAttribute("materia", materia);
		model.addAttribute("professor", professor);
		model.addAttribute("atividade", atividade);

		return "/professor/atividadecadastro";

	}
	@RequestMapping("/atividade/salvaredicao")
	public String atividadeSalvarEdicao(@Valid Atividade atividade, BindingResult result, RedirectAttributes attr, ModelMap model){

		Atividade ati = atividadeRepo.getOne(atividade.getId());
		Materia materia = materiaRepo.getOne(ati.getMateria().getId());
		Curso curso = cursoRepo.getOne(materia.getCurso().getId());
		Professor professor = repo.getOne(curso.getProfessor().getId());

			if (result.hasErrors()) {
				
				model.addAttribute("professor", professor);
				model.addAttribute("atividade", atividade);
				
				return "/professor/atividadeedicao";
			}
			atividade.setMateria(materia);
			atividadeRepo.save(atividade);
			
			attr.addFlashAttribute("message", "Atividade atualizada com sucesso!");
	
			return "redirect:/listaratividades/" + materia.getId();
	}

	@RequestMapping("/atividade/salvar")
	public String atividadeSalvar(@Valid Atividade atividade, BindingResult result, RedirectAttributes attr,
			ModelMap model) throws DataIntegrityViolationException {

		Materia materia = materiaRepo.getOne(atividade.getId());
		Curso curso = cursoRepo.getOne(materia.getCurso().getId());
		Professor professor = repo.getOne(curso.getProfessor().getId());
	
			if (result.hasErrors()) {

				model.addAttribute("materia", materia);
				model.addAttribute("professor", professor);
				model.addAttribute("atividade", atividade);
				return "/professor/atividadecadastro";
			}

			Atividade ati = new Atividade(null, atividade.getNome(), 0.0f, null, atividade.getAnotacoes(),
					atividade.getQuantidadeAlunosGrupo(), atividade.getDataCriacao(), atividade.getDataEntrega(),
					materia);
			atividadeRepo.save(ati);

			attr.addFlashAttribute("message", "Atividade cadastrado com sucesso!");
			return "redirect:/preparacadastroatividade/" + materia.getId();
	}
	
	@GetMapping("/atividade/excluir/{id}")
	public String excluiCurso(@PathVariable("id") Integer id, RedirectAttributes attr) {
		
           Atividade atividade = atividadeRepo.getOne(id);
           Materia materia = materiaRepo.getOne(atividade.getMateria().getId());
			atividadeRepo.deleteById(id);
			attr.addFlashAttribute("message", "Atividade excluida com sucesso!");
			
		return "redirect:/listaratividades/" + materia.getId();
	}
}
