package com.devgroup.professor_adm.recursos;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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
	public String listarProfessores(@PathVariable("id") Integer id, ModelMap model) {

		Aluno aluno = repo.getOne(id);

		List<Professor> professores = proRepository.findAll();

		model.addAttribute("aluno", aluno);
		model.addAttribute("professores", professores);

		return "/aluno/listarprofessores";
	}

	@GetMapping("/listardisciplinasgrupo/{id}")
	public String listarDisciplinasGrupo(@PathVariable("id") Integer id, ModelMap model) {

		Aluno aluno = repo.getOne(id);

		List<Materia> materias = aluno.getMateria();

		model.addAttribute("aluno", aluno);
		model.addAttribute("materia", materias);

		return "/aluno/administrargrupos";
	}

	@GetMapping("/cursosprofessor/{idpro}/{idalu}")
	public String listarCursos(@PathVariable("idpro") Integer idPro, @PathVariable("idalu") Integer idAlu,
			RedirectAttributes attr, ModelMap model) {

		Professor professor = proRepository.getOne(idPro);
		List<Curso> cursos = professor.getCursos();
		Aluno aluno = repo.getOne(idAlu);

		model.addAttribute("cursos", cursos);
		model.addAttribute("aluno", aluno);

		return "/aluno/listarcursos";
	}

	@GetMapping("/validacaocodigo/{iddisc}/{idalu}")
	public String validarCodigoAcesso(@PathVariable("iddisc") Integer iddisc, @PathVariable("idalu") Integer idAlu,
			ModelMap model) {

		Curso curso = cursoRepository.getOne(iddisc);
		Aluno aluno = repo.getOne(idAlu);

		aluno.setRgm(String.valueOf(curso.getId()));
		model.addAttribute("aluno", aluno);

		return "/aluno/codigocurso";
	}

	@GetMapping("/mostrarnotas/{iddisc}/{idalu}")
	public String mostrarNotas(@PathVariable("iddisc") Integer id, @PathVariable("idalu") Integer alunoId,
			RedirectAttributes attr, ModelMap model) {

		Materia materia = materiaRepository.getOne(id);
		Aluno aluno = repo.getOne(alunoId);
		List<AtividadeAluno> atividadeAluno = atividadeAlunoRepository.findAlunoAtividades(materia.getId(),
				aluno.getId());
		List<Atividade> atividade = materia.getAtividade();

		if (materia.getLiberarNota()) {
			model.addAttribute("materia", materia);
			model.addAttribute("aluno", aluno);
			model.addAttribute("atividadesaluno", atividadeAluno);
			model.addAttribute("atividade", atividade);
		} else {
			model.addAttribute("materia", materia);
			model.addAttribute("aluno", aluno);
			model.addAttribute("atividade", atividade);
			List<AtividadeAluno> alunos = new ArrayList<>();
			for (AtividadeAluno c : atividadeAluno) {
				AtividadeAluno alu = new AtividadeAluno(c.getId(), c.getNome(), 0.0f, null, null, c.getGrupo(),
						c.getAluno(), null, c.getMateria(), null);
				alu.setAtividade(c.getAtividade());

				alunos.add(alu);
			}
			model.addAttribute("atividadesaluno", alunos);
		}
		return "/aluno/notasaluno";
	}

	@RequestMapping("/aluno/validasenhacurso")
	public String validaçãoCurso(@Valid Aluno aluno, RedirectAttributes attr, ModelMap model) {

		Aluno alu = repo.getOne(aluno.getId());
		Curso curso = cursoRepository.getOne(Integer.parseInt(aluno.getRgm()));

		if (curso.getSenha().equals(aluno.getSenha())) {

			return "redirect:/disciplinasprofessor/" + curso.getId() + "/" + alu.getId();
		} else {
			attr.addFlashAttribute("messages", "Codigo de acesso invalido!");
			return "redirect:/cursosprofessor/" + curso.getProfessor().getId() + "/" + alu.getId();
		}
	}

	@GetMapping("/disciplinasprofessor/{iddisc}/{idalu}")
	public String listarDisciplina(@PathVariable("iddisc") Integer iddisc, @PathVariable("idalu") Integer idAlu,
			ModelMap model) {

		Curso curso = cursoRepository.getOne(iddisc);
		List<Materia> materias = curso.getMaterias();
		Aluno aluno = repo.getOne(idAlu);

		model.addAttribute("materia", materias);
		model.addAttribute("aluno", aluno);
		model.addAttribute("professor", curso.getProfessor());

		return "/aluno/listarmaterias";
	}

	@GetMapping("/criargrupos/{iddisc}/{idalu}")
	public String criarGrupos(@PathVariable("iddisc") Integer iddisc, @PathVariable("idalu") Integer idAlu,
			ModelMap model) {
		Aluno aluno = repo.getOne(idAlu);
		Materia materia = materiaRepository.getOne(iddisc);

		List<Atividade> atividade = atividadeRepository.findAtividadeComGrupo(materia.getId());

		model.addAttribute("aluno", aluno);
		model.addAttribute("materia", materia);
		model.addAttribute("atividade", atividade);

		return "/aluno/listaratividadegrupo";
	}

	@GetMapping("/cadastraremgrupo/{iddisc}/{idalu}")
	public String cadastrarEmGrupo(@PathVariable("iddisc") Integer iddisc, @PathVariable("idalu") Integer idAlu,
			ModelMap model) {

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

		model.addAttribute("aluno", aluno);
		model.addAttribute("materia", materia);
		model.addAttribute("atividadesalunos", atividadeAluno);
		model.addAttribute("atividade", atividade);
		model.addAttribute("atividadealuno", atiAluno);

		return "/aluno/atividadecadastrogrupo";
	}

	@GetMapping("/entrargrupoexistente/{iddisc}/{idalu}")
	public String entrarGrupoExistente(@PathVariable("iddisc") Integer iddisc, @PathVariable("idalu") Integer idAlu,
			ModelMap model) {

		AtividadeAluno amigoDeGrupo = atividadeAlunoRepository.getOne(iddisc);
		AtividadeAluno aluno = atividadeAlunoRepository.getOne(idAlu);

		aluno.setGrupo(amigoDeGrupo.getGrupo());
		atividadeAlunoRepository.save(aluno);

		return "redirect:/criargrupos/" + aluno.getMateria().getId() + "/" + aluno.getAluno().getId();
	}

	@PostMapping("/salvarcadastrogrupo/")
	public String salvarCadastroGrupo(@Valid AtividadeAluno atiAluno) {

		AtividadeAluno alunoAti = atividadeAlunoRepository.getOne(atiAluno.getId());
		alunoAti.setGrupo(atiAluno.getGrupo());
		atividadeAlunoRepository.save(alunoAti);

		Aluno aluno = repo.getOne(alunoAti.getAluno().getId());
		Materia materia = materiaRepository.getOne(alunoAti.getMateria().getId());

		return "redirect:/criargrupos/" + materia.getId() + "/" + aluno.getId();
	}

	@GetMapping("/cadastraalunoprofessor/{id}")
	public String alunoProfessorForm(@PathVariable("id") Integer id, ModelMap model) {

		Materia materia = materiaRepository.getOne(id);

		AlunoProfessor aluno = new AlunoProfessor();

		List<AlunoProfessor> alunoProfessor = materia.getAlunoprofessor();

		model.addAttribute("aluno", aluno);
		model.addAttribute("materia", materia);
		model.addAttribute("alunoprofessor", alunoProfessor);
		return "/aluno/alunoprofessor";
	}
	
	@GetMapping("/editaalunoprofessor/{id}")
	public String editarAlunoProfessor(@PathVariable("id") Integer id, ModelMap model) {

		AlunoProfessor aluno = alunoProRepo.getOne(id);
		Materia materia = aluno.getMateria();
		List<AlunoProfessor> alunoProfessor = materia.getAlunoprofessor();

		model.addAttribute("aluno", aluno);
		model.addAttribute("materia", materia);
		model.addAttribute("alunoprofessor", alunoProfessor);
		return "/aluno/alunoprofessoreditar";
	}
	
	@GetMapping("/excluiralunoprofessor/{id}")
	public String excluirAlunoProfessor(@PathVariable("id") Integer id) {

		AlunoProfessor aluno = alunoProRepo.getOne(id);
		Materia materia = aluno.getMateria();
		List<AtividadeAluno> atividades = aluno.getAtividadeAluno();
		
		atividadeAlunoRepository.deleteAll(atividades);
		alunoProRepo.delete(aluno);

		return "redirect:/areaadministrativanotas/" + materia.getId();
	}

	@RequestMapping("/salvaalunoprofessor/")
	public String cadastraAlunoProfessor(@Valid AlunoProfessor aluno) {

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

		return "redirect:/areaadministrativanotas/" + materia.getId();
	}
	
	@RequestMapping("/salvaalunoprofessoredicao/")
	public String salvaEditarAlunoProfessor(@Valid AlunoProfessor aluno) {
       
		AlunoProfessor aluPro = alunoProRepo.getOne(aluno.getId());
		
		Materia materia = aluPro.getMateria();
		
		aluPro.setRgm(aluno.getRgm());
		aluPro.setNome(aluno.getNome());
		
		alunoProRepo.save(aluPro);
	
		return "redirect:/areaadministrativanotas/" + materia.getId();
	}

	@GetMapping("/disciplinaparticipar/{iddisc}/{idalu}")
	public String participarDisciplina(@PathVariable("iddisc") Integer iddisc, @PathVariable("idalu") Integer idAlu,
			RedirectAttributes attr, ModelMap model) {

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

			return "redirect:/disciplinasprofessor/" + curso.getId() + "/" + aluno.getId();

		} catch (Exception e) {

			attr.addFlashAttribute("messages", "Disciplina já Cadastrada!");
			return "redirect:/disciplinasprofessor/" + curso.getId() + "/" + aluno.getId();
		}
	}

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
			Aluno alu = repo.getOne(aluno.getId());
			alu.setNome(aluno.getNome());
			alu.setEmail(aluno.getEmail());
			alu.setSenha(aluno.getSenha());
			alu.setRgm(aluno.getRgm());
			repo.save(alu);
			attr.addFlashAttribute("message", "Aluno atualizado com sucesso!");

			List<Materia> materia = alu.getMateria();

			model.addAttribute("aluno", alu);
			model.addAttribute("materia", materia);

			return "/aluno/aluno_principal";

		} catch (Exception e) {
			attr.addFlashAttribute("messages", "Emails e RGMs já cadastrado no sistema não são permitidos");
			model.addAttribute("aluno", aluno);
			return "/aluno/editar_aluno";
		}
	}

	@GetMapping("aluno/principal/{id}")
	public String telaPrincipal(@PathVariable("id") Integer id, Aluno aluno, ModelMap model) {

		Aluno alu = repo.getOne(id);

		List<Materia> materia = alu.getMateria();

		model.addAttribute("aluno", alu);
		model.addAttribute("materia", materia);

		return "/aluno/aluno_principal";
	}

	@RequestMapping("/aluno/editarfoto")
	public String editarFoto(@RequestParam("file") MultipartFile file, @Valid Aluno aluno, RedirectAttributes attr,
			ModelMap model) {

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
			model.addAttribute("aluno", alu);
			return "/aluno/editar_aluno";
		} catch (Exception e) {
			attr.addFlashAttribute("messages", "Erro. foto nao alterada!");
			model.addAttribute("aluno", alu);
			return "/aluno/editar_aluno";
		}
	}
}