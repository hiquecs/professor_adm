package com.devgroup.professor_adm.recursos;

import java.awt.image.BufferedImage;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.devgroup.professor_adm.Repositorios.AtividadeAlunoRepository;
import com.devgroup.professor_adm.Repositorios.CursoRepository;
import com.devgroup.professor_adm.Repositorios.MateriaRepository;
import com.devgroup.professor_adm.Repositorios.ProfessorRepository;
import com.devgroup.professor_adm.dominio.Aluno;
import com.devgroup.professor_adm.dominio.Atividade;
import com.devgroup.professor_adm.dominio.AtividadeAluno;
import com.devgroup.professor_adm.dominio.Curso;
import com.devgroup.professor_adm.dominio.Materia;
import com.devgroup.professor_adm.dominio.Professor;
import com.devgroup.professor_adm.servicos.ImageService;
import com.devgroup.professor_adm.servicos.S3Service;

@Controller
public class ProfessorRecursos {

	@Autowired
	private ProfessorRepository repo;

	@Autowired
	private CursoRepository cursoRepo;

	@Autowired
	private MateriaRepository materiaRepo;

	@Autowired
	private AtividadeAlunoRepository atiAlunoRepo;

	@Autowired
	private S3Service salva;

	@Autowired
	private ImageService imageService;

	@GetMapping("professor/editar/{id}")
	public ModelAndView preEditar(@PathVariable("id") Integer id, ModelAndView view) {

		Professor professor = repo.getOne(id);

		view.addObject("professor", professor);
		view.setViewName("/professor/editar_professor");

		return view;
	}

	@GetMapping("/areaadministrativa/{id}")
	public ModelAndView areaAdministrativa(@PathVariable("id") Integer id, ModelAndView view) {

		Professor professor = repo.getOne(id);

		view.addObject("professor", professor);
		view.setViewName("/professor/listarcursosadministracao");

		return view;
	}

	@GetMapping("/listarcursosadmin/{id}")
	public ModelAndView listarCursosAdmin(@PathVariable("id") Integer id, ModelAndView view) {

		Curso curso = cursoRepo.getOne(id);
		Professor professor = repo.getOne(curso.getProfessor().getId());

		view.addObject("professor", professor);
		view.addObject("curso", curso);
		view.setViewName("/professor/listarmateriasadministracao");

		return view;
	}

	@GetMapping("/editar/notasMateria/{id}")
	public ModelAndView editarNotas(@PathVariable("id") Integer id, ModelAndView view) {

		AtividadeAluno atividadeAluno = atiAlunoRepo.getOne(id);

		if (atividadeAluno.getAlunoProfessor() != null) {

			Aluno alu = new Aluno(null, atividadeAluno.getAlunoProfessor().getNome(), null, null,
					atividadeAluno.getAlunoProfessor().getRgm(), null, null);
			atividadeAluno.setAluno(alu);
		}

		view.addObject("atividade", atividadeAluno);
		view.setViewName("/professor/editarnotaatividades");

		return view;
	}

	@GetMapping("/editar/notasMateriagrupo/{id}")
	public ModelAndView editarNotasGrupo(@PathVariable("id") Integer id, ModelAndView view) {

		AtividadeAluno atividadeAluno = atiAlunoRepo.getOne(id);

		if (atividadeAluno.getAlunoProfessor() != null) {

			Aluno alu = new Aluno(null, atividadeAluno.getAlunoProfessor().getNome(), null, null,
					atividadeAluno.getAlunoProfessor().getRgm(), null, null);
			atividadeAluno.setAluno(alu);
		}

		view.addObject("atividade", atividadeAluno);
		view.setViewName("/professor/editarnotaatividadesgrupo");

		return view;
	}

	@PostMapping("/salvarnotaedicao")
	public ModelAndView salvarNotas(@Valid AtividadeAluno atividadeAluno, ModelAndView view) {

		AtividadeAluno ati = atiAlunoRepo.getOne(atividadeAluno.getId());

		ati.setNota(atividadeAluno.getNota());
		ati.setAnotacao(atividadeAluno.getAnotacao());
		ati.setSituacao(atividadeAluno.getSituacao());

		atiAlunoRepo.save(ati);
		view.setViewName("redirect:/areaadministrativanotas/" + ati.getMateria().getId());

		return view;
	}

	@PostMapping("/salvarnotaedicaogrupo")
	public ModelAndView salvarNotasGrupo(@Valid AtividadeAluno atividadeAluno, ModelAndView view) {

		AtividadeAluno ati = atiAlunoRepo.getOne(atividadeAluno.getId());

		List<AtividadeAluno> listAlunos = atiAlunoRepo.findAtividadePorNomeGrupo(ati.getAtividade().getId(),
				ati.getGrupo());

		for (AtividadeAluno c : listAlunos) {
			c.setNota(atividadeAluno.getNota());
			c.setAnotacao(atividadeAluno.getAnotacao());
			c.setSituacao(atividadeAluno.getSituacao());
		}
		Atividade atividade = ati.getAtividade();

		atiAlunoRepo.saveAll(listAlunos);

		if (ati.getGrupo() == null) {
			ati.setNota(atividadeAluno.getNota());
			ati.setAnotacao(atividadeAluno.getAnotacao());
			ati.setSituacao(atividadeAluno.getSituacao());
			atiAlunoRepo.save(ati);
		}
		view.setViewName("redirect:/listarporgrupos/" + atividade.getId());

		return view;
	}

	@GetMapping("/voltarareaadministrativanotas/{id}")
	public ModelAndView voltar(@PathVariable("id") Integer id, ModelAndView view) {

		AtividadeAluno ati = atiAlunoRepo.getOne(id);
		view.setViewName("redirect:/areaadministrativanotas/" + ati.getMateria().getId());

		return view;
	}

	@GetMapping("/areaadministrativanotas/{id}")
	public ModelAndView areaAdministrativaNotas(@PathVariable("id") Integer id, ModelAndView view) {

		Materia materia = materiaRepo.getOne(id);
		Professor professor = repo.getOne(materia.getCurso().getProfessor().getId());

		List<AtividadeAluno> atividades = atiAlunoRepo.findAtividades(materia.getId());
		List<Aluno> aluno = materia.getAlunos();

		for (AtividadeAluno c : atividades) {

			if (c.getAlunoProfessor() != null) {

				Aluno alu = new Aluno(null, c.getAlunoProfessor().getNome(), null, null, c.getAlunoProfessor().getRgm(),
						null, null);
				c.setAluno(alu);
			}
		}

		view.addObject("quantidade", aluno.size());
		view.addObject("materia", materia);
		view.addObject("atividade", atividades);
		view.addObject("professor", professor);
		view.setViewName("/professor/listaadministranotas");

		return view;
	}

	@GetMapping("/liberanota/{id}")
	public ModelAndView liberaNota(@PathVariable("id") Integer id, ModelAndView view) {

		Materia materia = materiaRepo.getOne(id);
		Boolean a = materia.getLiberarNota();
		if (a) {
			materia.setLiberarNota(false);
			materiaRepo.save(materia);
		} else {
			materia.setLiberarNota(true);
			materiaRepo.save(materia);
		}
		view.setViewName("redirect:/areaadministrativanotas/" + materia.getId());
		return view;
	}

	@GetMapping("/professor/preparalistaaluno/{id}")
	public ModelAndView preparaListaAluno(@PathVariable("id") Integer id, ModelAndView view) {

		Curso curso = cursoRepo.getOne(id);

		view.addObject("curso", curso);
		view.setViewName("/professor/listarmateriasalunos");

		return view;
	}

	@PostMapping("/professor/salvaredicao")
	public ModelAndView editarProfessor(@Valid Professor professor, BindingResult result, RedirectAttributes attr,
			ModelAndView view) {

		try {

			if (result.hasErrors()) {

				attr.addFlashAttribute("messages", "Emails já cadastrados no sistema não são permitidos");
				view.addObject("professor", professor);
				view.setViewName("/professor/editar_professor");

				return view;
			}

			Professor pro = repo.getOne(professor.getId());
			pro.setEmail(professor.getEmail());
			pro.setNome(professor.getNome());
			pro.setSenha(professor.getSenha());
			repo.save(pro);
			attr.addFlashAttribute("message", "Professor atualizado com sucesso!");

			view.addObject("professor", pro);
			view.setViewName("/professor/professor_principal");

			return view;

		} catch (Exception e) {
			attr.addFlashAttribute("messages", "Emails já cadastrados no sistema não são permitidos");
			view.addObject("professor", professor);
			view.setViewName("/professor/editar_professor");

			return view;
		}
	}

	@GetMapping("professor/principal/{id}")
	public ModelAndView telaPrincipal(@PathVariable("id") Integer id, ModelAndView view) {

		Professor professor = repo.getOne(id);

		view.addObject("professor", professor);
		view.setViewName("/professor/professor_principal");

		return view;
	}

	@PostMapping("/professor/editarfoto")
	public ModelAndView editarFoto(@RequestParam("file") MultipartFile file, @Valid Professor professor,
			RedirectAttributes attr, ModelAndView view) {

		Professor pro = repo.getOne(professor.getId());
		try {
			BufferedImage jpgImage = imageService.getJpgImageFromFile(file);
			jpgImage = imageService.cropSquare(jpgImage);
			jpgImage = imageService.resize(jpgImage, 200);
			String fileName = professor.getEmail() + ".jpg";
			String a = "" + salva.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");
			pro.setUrl(a);
			repo.save(pro);
			attr.addFlashAttribute("message", "Foto alterada com sucesso!");
			view.addObject("professor", pro);
			view.setViewName("/professor/editar_professor");

			return view;
		} catch (Exception e) {
			attr.addFlashAttribute("messages", "Erro. foto nao alterada!");
			view.addObject("professor", pro);
			view.setViewName("/professor/editar_professor");

			return view;
		}

	}

	@GetMapping("/trazeralunomateria/{id}")
	public ModelAndView trazerAlunosMateria(@PathVariable("id") Integer id, ModelAndView view) {

		Materia materia = materiaRepo.getOne(id);
		Professor professor = repo.getOne(materia.getCurso().getProfessor().getId());

		List<Aluno> alunos = materia.getAlunos();

		view.addObject("alunos", alunos);
		view.addObject("professor", professor);
		view.setViewName("/professor/listaralunos");

		return view;
	}
}