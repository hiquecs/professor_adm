package com.devgroup.professor_adm.recursos;

import java.util.List;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
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
	public ModelAndView selecionaCurso(@PathVariable("id") Integer id, ModelAndView view) {

		Professor professor = repo.getOne(id);

		view.addObject("professor", professor);
		view.setViewName("/professor/listarcursosatividades");

		return view;
	}

	@GetMapping("/escolhemateria/{id}")
	public ModelAndView selecionaMateria(@PathVariable("id") Integer id, ModelAndView view) {

		Curso curso = cursoRepo.getOne(id);

		Professor professor = repo.getOne(curso.getProfessor().getId());

		view.addObject("professor", professor);
		view.addObject("curso", curso);
		view.setViewName("/professor/listarmateriasatividades");

		return view;
	}

	@GetMapping("/informacaoatividade/{iddisc}/{idalu}")
	public ModelAndView mostrarNotas(@PathVariable("iddisc") Integer id, @PathVariable("idalu") Integer materiaId,
			ModelAndView view) {

		AtividadeAluno atividade = alunoRepo.getOne(id);
		Materia materia = materiaRepo.getOne(materiaId);
		Aluno aluno = repoAluno.getOne(atividade.getAluno().getId());
		Atividade ati = atividade.getAtividade();

		String a = (ati.getDataCriacao().toString());

		String b = (ati.getDataEntrega().toString());

		a = a.substring(0, 11);
		b = b.substring(0, 11);

		view.addObject("aluno", aluno);
		view.addObject("materia", materia);
		view.addObject("atividade", ati);
		view.addObject("b", b);
		view.addObject("a", a);
		view.setViewName("/aluno/informacoes");

		return view;
	}

	@GetMapping("/listaratividades/{id}")
	public ModelAndView selecionaAtividades(@PathVariable("id") Integer id, ModelAndView view) {

		Materia materia = materiaRepo.getOne(id);
		Professor professor = repo.getOne(materia.getCurso().getProfessor().getId());

		view.addObject("professor", professor);
		view.addObject("materia", materia);
		view.setViewName("/professor/listaratividades");

		return view;
	}

	@GetMapping("/listarporgrupos/{id}")
	public ModelAndView listarPorGrupos(@PathVariable("id") Integer id, ModelAndView view) {

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

		view.addObject("alunos", alunos);
		view.addObject("professor", professor);
		view.addObject("materia", materia);
		view.setViewName("/professor/administranotasporgrupo");

		return view;
	}

	@GetMapping("/listarasatividadesgrupo/{id}")
	public ModelAndView listarAsAtividades(@PathVariable("id") Integer id, ModelAndView view) {

		Materia materia = materiaRepo.getOne(id);

		List<Atividade> atividades = atividadeRepo.findAtividadeComGrupo(materia.getId());
		view.addObject("atividades", atividades);
		view.addObject("materia", materia);
		view.setViewName("/professor/listaratividadesgrupo");

		return view;
	}

	@GetMapping("/editar/atividade/{id}")
	public ModelAndView selecionaAtividade(@PathVariable("id") Integer id, ModelAndView view) {

		Atividade atividade = atividadeRepo.getOne(id);
		Materia materia = materiaRepo.getOne(atividade.getMateria().getId());
		Curso curso = cursoRepo.getOne(materia.getCurso().getId());
		Professor professor = repo.getOne(curso.getProfessor().getId());

		view.addObject("professor", professor);
		view.addObject("atividade", atividade);
		view.setViewName("/professor/atividadeedicao");

		return view;
	}

	@GetMapping("/preparacadastroatividade/{id}")
	public ModelAndView preparaCadastroAtividade(@PathVariable("id") Integer id, Atividade atividade,
			ModelAndView view) {

		Materia materia = materiaRepo.getOne(id);
		Professor professor = repo.getOne(materia.getCurso().getProfessor().getId());

		view.addObject("materia", materia);
		view.addObject("professor", professor);
		view.addObject("atividade", atividade);
		view.setViewName("/professor/atividadecadastro");

		return view;
	}

	@PostMapping("/atividade/salvaredicao")
	public ModelAndView atividadeSalvarEdicao(@Valid Atividade atividade, BindingResult result, RedirectAttributes attr,
			ModelAndView view) {

		Atividade ati = atividadeRepo.getOne(atividade.getId());
		Materia materia = materiaRepo.getOne(ati.getMateria().getId());
		Curso curso = cursoRepo.getOne(materia.getCurso().getId());
		Professor professor = repo.getOne(curso.getProfessor().getId());

		if (result.hasErrors()) {

			view.addObject("professor", professor);
			view.addObject("atividade", ati);
			view.setViewName("/professor/atividadeedicao");

			return view;
		}

		if (atividade.getDataCriacao() == null || atividade.getDataEntrega() == null) {

			view.addObject("materia", materia);
			view.addObject("professor", professor);
			view.addObject("atividade", atividade);
			view.setViewName("redirect:/editar/atividade/" + atividade.getId());

			attr.addFlashAttribute("messages", "Os Campos Datas são obrigatorios");

			return view;
		}

		if (atividade.getQuantidadeAlunosGrupo() > materia.getQuantidadeAlunos()) {

			view.addObject("materia", materia);
			view.addObject("professor", professor);
			view.addObject("atividade", atividade);
			view.setViewName("redirect:/editar/atividade/" + atividade.getId());

			attr.addFlashAttribute("messages",
					"Impossivel criar grupos maiores que a quantidade de alunos da Disciplina");

			return view;
		}

		if (atividade.getDataCriacao().after(atividade.getDataEntrega())) {

			view.addObject("materia", materia);
			view.addObject("professor", professor);
			view.addObject("atividade", atividade);
			view.setViewName("\"redirect:/editar/atividade/\" + atividade.getId()");

			attr.addFlashAttribute("messages", "Data de criação deve ser anterior a data de entrega");

			return view;
		}

		atividade.setUrl(ati.getUrl());
		atividade.setMateria(materia);
		atividadeRepo.save(atividade);
		view.setViewName("redirect:/listaratividades/" + materia.getId());

		attr.addFlashAttribute("message", "Atividade atualizada com sucesso!");

		return view;
	}

	@PostMapping("/atividade/salvar")
	public ModelAndView atividadeSalvar(@RequestParam("file") MultipartFile file, @Valid Atividade atividade,
			BindingResult result, RedirectAttributes attr, ModelAndView view) throws DataIntegrityViolationException {

		Materia materia = materiaRepo.getOne(atividade.getId());
		Curso curso = cursoRepo.getOne(materia.getCurso().getId());
		Professor professor = repo.getOne(curso.getProfessor().getId());

		if (result.hasErrors()) {

			view.addObject("materia", materia);
			view.addObject("professor", professor);
			view.addObject("atividade", atividade);
			view.setViewName("/professor/atividadecadastro");

			return view;
		}
		if (atividade.getDataCriacao() == null || atividade.getDataEntrega() == null) {

			view.addObject("materia", materia);
			view.addObject("professor", professor);
			view.addObject("atividade", atividade);
			view.setViewName("redirect:/preparacadastroatividade/" + materia.getId());

			attr.addFlashAttribute("messages", "Os Campos Datas são obrigatorios");

			return view;
		}

		if (atividade.getQuantidadeAlunosGrupo() > materia.getQuantidadeAlunos()) {

			view.addObject("materia", materia);
			view.addObject("professor", professor);
			view.addObject("atividade", atividade);
			view.setViewName("redirect:/preparacadastroatividade/" + materia.getId());

			attr.addFlashAttribute("messages",
					"Impossivel criar grupos maiores que a quantidade de alunos da Disciplina");

			return view;
		}

		if (atividade.getDataCriacao().after(atividade.getDataEntrega())) {

			view.addObject("materia", materia);
			view.addObject("professor", professor);
			view.addObject("atividade", atividade);
			view.setViewName("redirect:/preparacadastroatividade/" + materia.getId());

			attr.addFlashAttribute("messages", "Data de criação deve ser anterior a data de entrega");

			return view;
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
		view.setViewName("redirect:/preparacadastroatividade/" + materia.getId());

		return view;
	}

	@GetMapping("/atividade/excluir/{id}")
	public ModelAndView excluiCurso(@PathVariable("id") Integer id, RedirectAttributes attr, ModelAndView view) {

		Atividade atividade = atividadeRepo.getOne(id);
		Materia materia = materiaRepo.getOne(atividade.getMateria().getId());
		atividadeRepo.deleteById(id);
		attr.addFlashAttribute("message", "Atividade excluida com sucesso!");
		view.setViewName("redirect:/listaratividades/" + materia.getId());

		return view;
	}

	@PostMapping("/atividade/editararquivo")
	public ModelAndView editarArquivo(@RequestParam("file") MultipartFile file, @Valid Atividade atividade,
			RedirectAttributes attr, ModelAndView view) {

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
			view.setViewName("redirect:/editar/atividade/" + ati.getId());
			return view;
		} catch (Exception e) {
			attr.addFlashAttribute("messages", "Erro. atividade nao alterada!");
			view.setViewName("redirect:/editar/atividade/" + ati.getId());

			return view;
		}
	}
}