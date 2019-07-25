package com.devgroup.professor_adm.recursos;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.devgroup.professor_adm.Repositorios.AlunoProfessorRepository;
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
import com.devgroup.professor_adm.servicos.ImageService;
import com.devgroup.professor_adm.servicos.S3Service;

@Controller
public class AlunoRecursos {

	@Autowired
	private AlunoRepository repo;

	@Autowired
	private S3Service salva;

	@Autowired
	private ImageService imageService;

	@Autowired
	private ProfessorRepository proRepository;

	@Autowired
	private CursoRepository cursoRepository;

	@Autowired
	private MateriaRepository materiaRepository;

	@Autowired
	private AlunoProfessorRepository alunoProRepo;

	@Autowired
	private AtividadeRepository atividadeRepository;

	@Autowired
	private AtividadeAlunoRepository atividadeAlunoRepository;

	@GetMapping("/listaprofessores/{id}")
	public ModelAndView listarProfessores(@PathVariable("id") Integer id, ModelAndView view) {

		Aluno aluno = repo.getOne(id);

		List<Professor> professores = proRepository.findAll();

		view.addObject("aluno", aluno);
		view.addObject("professores", professores);
		view.setViewName("/aluno/listarprofessores");

		return view;
	}

	@GetMapping("/listardisciplinasgrupo/{id}")
	public ModelAndView listarDisciplinasGrupo(@PathVariable("id") Integer id, ModelAndView view) {

		Aluno aluno = repo.getOne(id);

		List<Materia> materias = aluno.getMateria();

		view.addObject("aluno", aluno);
		view.addObject("materia", materias);
		view.setViewName("/aluno/administrargrupos");

		return view;
	}

	@GetMapping("/cursosprofessor/{idpro}/{idalu}")
	public ModelAndView listarCursos(@PathVariable("idpro") Integer idPro, @PathVariable("idalu") Integer idAlu,
			ModelAndView view) {

		Professor professor = proRepository.getOne(idPro);
		List<Curso> cursos = professor.getCursos();
		Aluno aluno = repo.getOne(idAlu);

		view.addObject("cursos", cursos);
		view.addObject("aluno", aluno);
		view.setViewName("/aluno/listarcursos");

		return view;
	}

	@GetMapping("/validacaocodigo/{iddisc}/{idalu}")
	public ModelAndView validarCodigoAcesso(@PathVariable("iddisc") Integer iddisc,
			@PathVariable("idalu") Integer idAlu, ModelAndView view) {

		Curso curso = cursoRepository.getOne(iddisc);
		Aluno aluno = repo.getOne(idAlu);

		aluno.setRgm(String.valueOf(curso.getId()));
		view.addObject("aluno", aluno);
		view.setViewName("/aluno/codigocurso");

		return view;
	}

	@GetMapping("/mostrarnotas/{iddisc}/{idalu}")
	public ModelAndView mostrarNotas(@PathVariable("iddisc") Integer id, @PathVariable("idalu") Integer alunoId,
			ModelAndView view) {

		Materia materia = materiaRepository.getOne(id);
		Aluno aluno = repo.getOne(alunoId);
		List<AtividadeAluno> atividadeAluno = atividadeAlunoRepository.findAlunoAtividades(materia.getId(),
				aluno.getId());
		List<Atividade> atividade = materia.getAtividade();

		if (materia.getLiberarNota()) {
			view.addObject("materia", materia);
			view.addObject("aluno", aluno);
			view.addObject("atividadesaluno", atividadeAluno);
			view.addObject("atividade", atividade);
		} else {
			view.addObject("materia", materia);
			view.addObject("aluno", aluno);
			view.addObject("atividade", atividade);
			List<AtividadeAluno> alunos = new ArrayList<>();
			for (AtividadeAluno c : atividadeAluno) {
				AtividadeAluno alu = new AtividadeAluno(c.getId(), c.getNome(), 0.0f, null, null, c.getGrupo(),
						c.getAluno(), null, c.getMateria(), null);
				alu.setAtividade(c.getAtividade());

				alunos.add(alu);
			}
			view.addObject("atividadesaluno", alunos);
			view.setViewName("/aluno/notasaluno");
		}
		return view;
	}

	@PostMapping("/aluno/validasenhacurso")
	public ModelAndView validaçãoCurso(@Valid Aluno aluno, RedirectAttributes attr, ModelAndView view) {

		Aluno alu = repo.getOne(aluno.getId());
		Curso curso = cursoRepository.getOne(Integer.parseInt(aluno.getRgm()));

		if (curso.getSenha().equals(aluno.getSenha())) {

			view.setViewName("redirect:/disciplinasprofessor/" + curso.getId() + "/" + alu.getId());

			return view;
		} else {
			attr.addFlashAttribute("messages", "Codigo de acesso invalido!");
			view.setViewName("redirect:/cursosprofessor/" + curso.getProfessor().getId() + "/" + alu.getId());

			return view;
		}
	}

	@GetMapping("/disciplinasprofessor/{iddisc}/{idalu}")
	public ModelAndView listarDisciplina(@PathVariable("iddisc") Integer iddisc, @PathVariable("idalu") Integer idAlu,
			ModelAndView view) {

		Curso curso = cursoRepository.getOne(iddisc);
		List<Materia> materias = curso.getMaterias();
		Aluno aluno = repo.getOne(idAlu);

		view.addObject("materia", materias);
		view.addObject("aluno", aluno);
		view.addObject("professor", curso.getProfessor());
		view.setViewName("/aluno/listarmaterias");

		return view;
	}

	@GetMapping("/criargrupos/{iddisc}/{idalu}")
	public ModelAndView criarGrupos(@PathVariable("iddisc") Integer iddisc, @PathVariable("idalu") Integer idAlu,
			ModelAndView view) {
		Aluno aluno = repo.getOne(idAlu);
		Materia materia = materiaRepository.getOne(iddisc);

		List<Atividade> atividade = atividadeRepository.findAtividadeComGrupo(materia.getId());

		view.addObject("aluno", aluno);
		view.addObject("materia", materia);
		view.addObject("atividade", atividade);
		view.setViewName("/aluno/listaratividadegrupo");

		return view;
	}

	@GetMapping("/cadastraremgrupo/{iddisc}/{idalu}")
	public ModelAndView cadastrarEmGrupo(@PathVariable("iddisc") Integer iddisc, @PathVariable("idalu") Integer idAlu,
			ModelAndView view) {

		Aluno aluno = repo.getOne(idAlu);
		Atividade atividade = atividadeRepository.getOne(iddisc);
		Materia materia = atividade.getMateria();

		List<AtividadeAluno> atividadeAlunos = atividadeAlunoRepository.findGrupos(materia.getId(), atividade.getId());
		List<AtividadeAluno> atividadeAluno = new ArrayList<>();

		for (AtividadeAluno c : atividadeAlunos) {

			if (c.getAluno() != null) {
				atividadeAluno.add(c);
			}
		}
		AtividadeAluno atiAluno = atividadeAlunoRepository.findAtividadeAluno(materia.getId(), aluno.getId(),
				atividade.getId());

		view.addObject("aluno", aluno);
		view.addObject("materia", materia);
		view.addObject("atividadesalunos", atividadeAluno);
		view.addObject("atividade", atividade);
		view.addObject("atividadealuno", atiAluno);
		view.setViewName("/aluno/atividadecadastrogrupo");

		return view;
	}

	@GetMapping("/entrargrupoexistente/{iddisc}/{idalu}")
	public ModelAndView entrarGrupoExistente(@PathVariable("iddisc") Integer iddisc,
			@PathVariable("idalu") Integer idAlu, ModelAndView view) {

		AtividadeAluno amigoDeGrupo = atividadeAlunoRepository.getOne(iddisc);
		AtividadeAluno aluno = atividadeAlunoRepository.getOne(idAlu);

		aluno.setGrupo(amigoDeGrupo.getGrupo());
		atividadeAlunoRepository.save(aluno);
		view.setViewName("redirect:/criargrupos/" + aluno.getMateria().getId() + "/" + aluno.getAluno().getId());

		return view;
	}

	@PostMapping("/salvarcadastrogrupo/")
	public ModelAndView salvarCadastroGrupo(@Valid AtividadeAluno atiAluno, ModelAndView view) {

		AtividadeAluno alunoAti = atividadeAlunoRepository.getOne(atiAluno.getId());
		alunoAti.setGrupo(atiAluno.getGrupo());
		atividadeAlunoRepository.save(alunoAti);

		Aluno aluno = repo.getOne(alunoAti.getAluno().getId());
		Materia materia = materiaRepository.getOne(alunoAti.getMateria().getId());
		view.setViewName("redirect:/criargrupos/" + materia.getId() + "/" + aluno.getId());

		return view;
	}

	@GetMapping("/cadastraalunoprofessor/{id}")
	public ModelAndView alunoProfessorForm(@PathVariable("id") Integer id, ModelAndView view) {

		Materia materia = materiaRepository.getOne(id);

		AlunoProfessor aluno = new AlunoProfessor();

		List<AlunoProfessor> alunoProfessor = materia.getAlunoprofessor();

		view.addObject("aluno", aluno);
		view.addObject("materia", materia);
		view.addObject("alunoprofessor", alunoProfessor);
		view.setViewName("/aluno/alunoprofessor");

		return view;
	}

	@GetMapping("/editaalunoprofessor/{id}")
	public ModelAndView editarAlunoProfessor(@PathVariable("id") Integer id, ModelAndView view) {

		AlunoProfessor aluno = alunoProRepo.getOne(id);
		Materia materia = aluno.getMateria();
		List<AlunoProfessor> alunoProfessor = materia.getAlunoprofessor();

		view.addObject("aluno", aluno);
		view.addObject("materia", materia);
		view.addObject("alunoprofessor", alunoProfessor);
		view.setViewName("/aluno/alunoprofessoreditar");

		return view;
	}

	@GetMapping("/excluiralunoprofessor/{id}")
	public ModelAndView excluirAlunoProfessor(@PathVariable("id") Integer id, ModelAndView view) {

		AlunoProfessor aluno = alunoProRepo.getOne(id);
		Materia materia = aluno.getMateria();
		List<AtividadeAluno> atividades = aluno.getAtividadeAluno();

		atividadeAlunoRepository.deleteAll(atividades);
		alunoProRepo.delete(aluno);
		view.setViewName("redirect:/areaadministrativanotas/" + materia.getId());

		return view;
	}

	@PostMapping("/salvaalunoprofessor/")
	public ModelAndView cadastraAlunoProfessor(@Valid AlunoProfessor aluno, ModelAndView view) {

		Materia materia = materiaRepository.getOne(aluno.getId());
		aluno.setId(null);

		List<Atividade> atividade = materia.getAtividade();

		aluno.setMateria(materia);

		materia.getAlunoprofessor().addAll(Arrays.asList(aluno));

		alunoProRepo.save(aluno);
		materiaRepository.save(materia);

		List<AtividadeAluno> ativi = new ArrayList<>();
		for (Atividade c : atividade) {

			AtividadeAluno ati = new AtividadeAluno(null, c.getNome(), 0.0f, null, false, null, null, aluno, materia,
					c);

			aluno.getAtividadeAluno().addAll(Arrays.asList(ati));
			ativi.add(ati);
			atividadeAlunoRepository.save(ati);
		}
		alunoProRepo.save(aluno);

		view.setViewName("redirect:/areaadministrativanotas/" + materia.getId());

		return view;
	}

	@PostMapping("/salvaalunoprofessoredicao/")
	public ModelAndView salvaEditarAlunoProfessor(@Valid AlunoProfessor aluno, ModelAndView view) {

		AlunoProfessor aluPro = alunoProRepo.getOne(aluno.getId());

		Materia materia = aluPro.getMateria();

		aluPro.setRgm(aluno.getRgm());
		aluPro.setNome(aluno.getNome());

		alunoProRepo.save(aluPro);
		view.setViewName("redirect:/areaadministrativanotas/" + materia.getId());

		return view;
	}

	@GetMapping("/disciplinaparticipar/{iddisc}/{idalu}")
	public ModelAndView participarDisciplina(@PathVariable("iddisc") Integer iddisc,
			@PathVariable("idalu") Integer idAlu, RedirectAttributes attr, ModelAndView view) {

		Materia materia = materiaRepository.getOne(iddisc);
		Curso curso = materia.getCurso();
		Aluno aluno = repo.getOne(idAlu);
		List<Atividade> atividade = materia.getAtividade();

		try {

			materia.getAlunos().addAll(Arrays.asList(aluno));
			aluno.getMateria().addAll(Arrays.asList(materia));

			materiaRepository.save(materia);
			repo.save(aluno);
			attr.addFlashAttribute("message", "Participação Efetuada com sucesso!");
			List<AtividadeAluno> ativi = new ArrayList<>();
			for (Atividade c : atividade) {

				AtividadeAluno ati = new AtividadeAluno(null, c.getNome(), 0.0f, null, false, null, aluno, null,
						materia, c);

				aluno.getAtividadeAluno().addAll(Arrays.asList(ati));
				ativi.add(ati);
				atividadeAlunoRepository.save(ati);
			}

			repo.save(aluno);
			view.setViewName("redirect:/disciplinasprofessor/" + curso.getId() + "/" + aluno.getId());

			return view;

		} catch (Exception e) {

			attr.addFlashAttribute("messages", "Disciplina já Cadastrada!");
			view.setViewName("redirect:/disciplinasprofessor/" + curso.getId() + "/" + aluno.getId());

			return view;
		}
	}

	@GetMapping("aluno/editar/{id}")
	public ModelAndView preEditar(@PathVariable("id") Integer id, ModelAndView view) {

		Aluno aluno = repo.getOne(id);

		view.addObject("aluno", aluno);
		view.setViewName("/aluno/editar_aluno");

		return view;
	}

	@PostMapping("/aluno/salvaredicao")
	public ModelAndView editarAluno(@Valid Aluno aluno, BindingResult result, RedirectAttributes attr,
			ModelAndView view) {

		try {

			if (result.hasErrors()) {

				attr.addFlashAttribute("messages", "Emails e RGMs já cadastrado no sistema não são permitidos");
				view.addObject("aluno", aluno);
				view.setViewName("/aluno/editar_aluno");

				return view;
			}
			Aluno alu = repo.getOne(aluno.getId());
			alu.setNome(aluno.getNome());
			alu.setEmail(aluno.getEmail());
			alu.setSenha(aluno.getSenha());
			alu.setRgm(aluno.getRgm());
			repo.save(alu);
			attr.addFlashAttribute("message", "Aluno atualizado com sucesso!");

			List<Materia> materia = alu.getMateria();

			view.addObject("aluno", alu);
			view.addObject("materia", materia);
			view.setViewName("/aluno/aluno_principal");

			return view;

		} catch (Exception e) {
			attr.addFlashAttribute("messages", "Emails e RGMs já cadastrado no sistema não são permitidos");
			view.addObject("aluno", aluno);
			view.setViewName("/aluno/editar_aluno");

			return view;
		}
	}

	@GetMapping("aluno/principal/{id}")
	public ModelAndView telaPrincipal(@PathVariable("id") Integer id, Aluno aluno, ModelAndView view) {

		Aluno alu = repo.getOne(id);

		List<Materia> materia = alu.getMateria();

		view.addObject("aluno", alu);
		view.addObject("materia", materia);
		view.setViewName("/aluno/aluno_principal");

		return view;
	}

	@PostMapping("/aluno/editarfoto")
	public ModelAndView editarFoto(@RequestParam("file") MultipartFile file, @Valid Aluno aluno,
			RedirectAttributes attr, ModelAndView view) {

		Aluno alu = repo.getOne(aluno.getId());
		try {
			BufferedImage jpgImage = imageService.getJpgImageFromFile(file);
			jpgImage = imageService.cropSquare(jpgImage);
			jpgImage = imageService.resize(jpgImage, 200);
			String fileName = alu.getRgm() + ".jpg";
			String a = "" + salva.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");
			alu.setUrl(a);
			repo.save(alu);
			attr.addFlashAttribute("message", "Foto alterada com sucesso!");
			view.addObject("aluno", alu);
			view.setViewName("/aluno/editar_aluno");

			return view;
		} catch (Exception e) {
			attr.addFlashAttribute("messages", "Erro. foto nao alterada!");
			view.addObject("aluno", alu);
			view.setViewName("/aluno/editar_aluno");
			return view;
		}
	}
}