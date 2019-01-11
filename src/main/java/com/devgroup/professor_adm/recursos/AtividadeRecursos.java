package com.devgroup.professor_adm.recursos;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
import com.devgroup.professor_adm.Repositorios.AtividadeAlunoRepository;
import com.devgroup.professor_adm.Repositorios.AtividadeRepository;
import com.devgroup.professor_adm.Repositorios.CursoRepository;
import com.devgroup.professor_adm.Repositorios.MateriaRepository;
import com.devgroup.professor_adm.Repositorios.ProfessorRepository;
import com.devgroup.professor_adm.dominio.Aluno;
import com.devgroup.professor_adm.dominio.AlunoProfessor;
import com.devgroup.professor_adm.dominio.Atividade;
import com.devgroup.professor_adm.dominio.AtividadeAluno;
import com.devgroup.professor_adm.dominio.Curso;
import com.devgroup.professor_adm.dominio.Materia;
import com.devgroup.professor_adm.dominio.Professor;
import com.devgroup.professor_adm.servicos.S3Service;

@Controller
public class AtividadeRecursos {

	@Autowired
	private ProfessorRepository repo;

	@Autowired
	private AlunoRepository repoAluno;

	@Autowired
	private CursoRepository cursoRepo;

	@Autowired
	private MateriaRepository materiaRepo;

	@Autowired
	private AtividadeRepository atividadeRepo;

	@Autowired
	private AtividadeAlunoRepository alunoRepo;

	@Autowired
	private S3Service salva;

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

	@GetMapping("/informacaoatividade/{iddisc}/{idalu}")
	public String mostrarNotas(@PathVariable("iddisc") Integer id, @PathVariable("idalu") Integer materiaId,
			ModelMap model) {

		AtividadeAluno atividade = alunoRepo.getOne(id);
		Materia materia = materiaRepo.getOne(materiaId);
		Aluno aluno = repoAluno.getOne(atividade.getAluno().getId());
		Atividade ati = atividade.getAtividade();
		
		@SuppressWarnings("deprecation")
		String a = (ati.getDataCriacao().toLocaleString());
		
		@SuppressWarnings("deprecation")
		String b = (ati.getDataEntrega().toLocaleString());
		
		a = a.substring(0,11);		
		b = b.substring(0,11);
		

		model.addAttribute("aluno", aluno);
		model.addAttribute("materia", materia);
		model.addAttribute("atividade", ati);
		model.addAttribute("b",b);
		model.addAttribute("a",a);

		return "/aluno/informacoes";
	}

	@GetMapping("/listaratividades/{id}")
	public String selecionaAtividades(@PathVariable("id") Integer id, ModelMap model) {

		Materia materia = materiaRepo.getOne(id);
		Professor professor = repo.getOne(materia.getCurso().getProfessor().getId());

		model.addAttribute("professor", professor);
		model.addAttribute("materia", materia);

		return "/professor/listaratividades";
	}

	@GetMapping("/listarporgrupos/{id}")
	public String listarPorGrupos(@PathVariable("id") Integer id, ModelMap model) {

		Atividade atividade = atividadeRepo.getOne(id);

		Materia materia = atividade.getMateria();

		Professor professor = materia.getCurso().getProfessor();

		List<AtividadeAluno> alunos = alunoRepo.findGrupos(atividade.getMateria().getId(), atividade.getId());

		for (AtividadeAluno c : alunos) {

			if (c.getAlunoProfessor() != null) {

				Aluno alu = new Aluno(null, c.getAlunoProfessor().getNome(), null, null, c.getAlunoProfessor().getRgm(),
						null, null);
				c.setAluno(alu);
			}
		}

		model.addAttribute("alunos", alunos);
		model.addAttribute("professor", professor);
		model.addAttribute("materia", materia);

		return "/professor/administranotasporgrupo";
	}

	@GetMapping("/listarasatividadesgrupo/{id}")
	public String listarAsAtividades(@PathVariable("id") Integer id, ModelMap model) {

		Materia materia = materiaRepo.getOne(id);

		List<Atividade> atividades = atividadeRepo.findAtividadeComGrupo(materia.getId());
		model.addAttribute("atividades", atividades);
		model.addAttribute("materia", materia);

		return "/professor/listaratividadesgrupo";
	}

	@GetMapping("/editar/atividade/{id}")
	public String selecionaAtividade(@PathVariable("id") Integer id, RedirectAttributes attr, ModelMap model) {

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
	public String atividadeSalvarEdicao(@Valid Atividade atividade, BindingResult result, RedirectAttributes attr,
			ModelMap model) {

		Atividade ati = atividadeRepo.getOne(atividade.getId());
		Materia materia = materiaRepo.getOne(ati.getMateria().getId());
		Curso curso = cursoRepo.getOne(materia.getCurso().getId());
		Professor professor = repo.getOne(curso.getProfessor().getId());

		if (result.hasErrors()) {

			model.addAttribute("professor", professor);
			model.addAttribute("atividade", ati);

			return "/professor/atividadeedicao";
		}

		if (atividade.getDataCriacao() == null || atividade.getDataEntrega() == null) {

			model.addAttribute("materia", materia);
			model.addAttribute("professor", professor);
			model.addAttribute("atividade", atividade);

			attr.addFlashAttribute("messages", "Os Campos Datas são obrigatorios");

			return "redirect:/editar/atividade/" + atividade.getId();
		}

		if (atividade.getQuantidadeAlunosGrupo() > materia.getQuantidadeAlunos()) {

			model.addAttribute("materia", materia);
			model.addAttribute("professor", professor);
			model.addAttribute("atividade", atividade);

			attr.addFlashAttribute("messages",
					"Impossivel criar grupos maiores que a quantidade de alunos da Disciplina");

			return "redirect:/editar/atividade/" + atividade.getId();
		}

		if (atividade.getDataCriacao().after(atividade.getDataEntrega())) {

			model.addAttribute("materia", materia);
			model.addAttribute("professor", professor);
			model.addAttribute("atividade", atividade);

			attr.addFlashAttribute("messages", "Data de criação deve ser anterior a data de entrega");

			return "redirect:/editar/atividade/" + atividade.getId();
		}

		atividade.setUrl(ati.getUrl());
		atividade.setMateria(materia);
		atividadeRepo.save(atividade);

		attr.addFlashAttribute("message", "Atividade atualizada com sucesso!");

		return "redirect:/listaratividades/" + materia.getId();
	}

	@RequestMapping("/atividade/salvar")
	public String atividadeSalvar(@RequestParam("file") MultipartFile file, @Valid Atividade atividade,
			BindingResult result, RedirectAttributes attr, ModelMap model) throws DataIntegrityViolationException {

		Materia materia = materiaRepo.getOne(atividade.getId());
		Curso curso = cursoRepo.getOne(materia.getCurso().getId());
		Professor professor = repo.getOne(curso.getProfessor().getId());

		if (result.hasErrors()) {

			model.addAttribute("materia", materia);
			model.addAttribute("professor", professor);
			model.addAttribute("atividade", atividade);
			return "/professor/atividadecadastro";
		}
		if (atividade.getDataCriacao() == null || atividade.getDataEntrega() == null) {

			model.addAttribute("materia", materia);
			model.addAttribute("professor", professor);
			model.addAttribute("atividade", atividade);

			attr.addFlashAttribute("messages", "Os Campos Datas são obrigatorios");

			return "redirect:/preparacadastroatividade/" + materia.getId();
		}

		if (atividade.getQuantidadeAlunosGrupo() > materia.getQuantidadeAlunos()) {

			model.addAttribute("materia", materia);
			model.addAttribute("professor", professor);
			model.addAttribute("atividade", atividade);

			attr.addFlashAttribute("messages",
					"Impossivel criar grupos maiores que a quantidade de alunos da Disciplina");

			return "redirect:/preparacadastroatividade/" + materia.getId();
		}

		if (atividade.getDataCriacao().after(atividade.getDataEntrega())) {

			model.addAttribute("materia", materia);
			model.addAttribute("professor", professor);
			model.addAttribute("atividade", atividade);

			attr.addFlashAttribute("messages", "Data de criação deve ser anterior a data de entrega");

			return "redirect:/preparacadastroatividade/" + materia.getId();
		}

		String a;

		if (!file.isEmpty()) {
			a = "" + salva.uploadFile(file);
			atividade.setUrl(a);
		} else {
			a = "https://s3-sa-east-1.amazonaws.com/professor-adm/anexoinexixtente.png";
		}

		Atividade ati = new Atividade(null, atividade.getNome(), atividade.getAnotacoes(),
				atividade.getQuantidadeAlunosGrupo(), atividade.getDataCriacao(), atividade.getDataEntrega(), a,
				materia);
		atividadeRepo.save(ati);

		if (materia.getAlunos() != null) {
			List<Aluno> alunos = materia.getAlunos();
			for (Aluno alu : alunos) {
				AtividadeAluno atividadeAluno = new AtividadeAluno(null, ati.getNome(), 0.0f, null, false, null, alu,
						null, materia, ati);

				alunoRepo.save(atividadeAluno);
			}

		}

		if (materia.getAlunoprofessor() != null) {
			List<AlunoProfessor> alunos = materia.getAlunoprofessor();
			for (AlunoProfessor alu : alunos) {
				AtividadeAluno atividadeAluno = new AtividadeAluno(null, ati.getNome(), 0.0f, null, false, null, null,
						alu, materia, ati);

				alunoRepo.save(atividadeAluno);
			}

		}

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

	@RequestMapping("/atividade/editararquivo")
	public String editarArquivo(@RequestParam("file") MultipartFile file, @Valid Atividade atividade,
			RedirectAttributes attr, ModelMap model) {

		Atividade ati = atividadeRepo.getOne(atividade.getId());

		try {

			if (!file.isEmpty()) {
				String a = "" + salva.uploadFile(file);
				ati.setUrl(a);
				atividadeRepo.save(ati);
				attr.addFlashAttribute("message", "Atividade alterada com sucesso!");
			} else {
				String a = "https://s3-sa-east-1.amazonaws.com/professor-adm/anexoinexixtente.png";
				ati.setUrl(a);
				atividadeRepo.save(ati);
				attr.addFlashAttribute("message", "Atividade alterada com sucesso!");
			}
			return "redirect:/editar/atividade/" + ati.getId();
		} catch (Exception e) {
			attr.addFlashAttribute("messages", "Erro. atividade nao alterada!");

			return "redirect:/editar/atividade/" + ati.getId();
		}
	}
}