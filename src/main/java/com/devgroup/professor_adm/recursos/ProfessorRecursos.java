package com.devgroup.professor_adm.recursos;

import java.awt.image.BufferedImage;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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
	public String preEditar(@PathVariable("id") Integer id, ModelMap model) {

		Professor professor = repo.getOne(id);

		model.addAttribute("professor", professor);
		return "/professor/editar_professor";
	}

	@GetMapping("/areaadministrativa/{id}")
	public String areaAdministrativa(@PathVariable("id") Integer id, ModelMap model) {

		Professor professor = repo.getOne(id);

		model.addAttribute("professor", professor);
		return "/professor/listarcursosadministracao";
	}

	@GetMapping("/listarcursosadmin/{id}")
	public String listarCursosAdmin(@PathVariable("id") Integer id, ModelMap model) {

		Curso curso = cursoRepo.getOne(id);
		Professor professor = repo.getOne(curso.getProfessor().getId());

		model.addAttribute("professor", professor);
		model.addAttribute("curso", curso);
		return "/professor/listarmateriasadministracao";
	}

	@GetMapping("/editar/notasMateria/{id}")
	public String editarNotas(@PathVariable("id") Integer id, ModelMap model) {

		AtividadeAluno atividadeAluno = atiAlunoRepo.getOne(id);

		if (atividadeAluno.getAlunoProfessor() != null) {

			Aluno alu = new Aluno(null, atividadeAluno.getAlunoProfessor().getNome(), null, null,
					atividadeAluno.getAlunoProfessor().getRgm(), null, null);
			atividadeAluno.setAluno(alu);
		}

		model.addAttribute("atividade", atividadeAluno);

		return "/professor/editarnotaatividades";
	}

	@GetMapping("/editar/notasMateriagrupo/{id}")
	public String editarNotasGrupo(@PathVariable("id") Integer id, ModelMap model) {

		AtividadeAluno atividadeAluno = atiAlunoRepo.getOne(id);
		
		

			if (atividadeAluno.getAlunoProfessor() != null) {

				Aluno alu = new Aluno(null, atividadeAluno.getAlunoProfessor().getNome(), null, null, atividadeAluno.getAlunoProfessor().getRgm(),
						null, null);
				atividadeAluno.setAluno(alu);
			}
		
		model.addAttribute("atividade", atividadeAluno);

		return "/professor/editarnotaatividadesgrupo";
	}

	@RequestMapping("/salvarnotaedicao")
	public String salvarNotas(@Valid AtividadeAluno atividadeAluno) {
	
		AtividadeAluno ati = atiAlunoRepo.getOne(atividadeAluno.getId());

		ati.setNota(atividadeAluno.getNota());
		ati.setAnotacao(atividadeAluno.getAnotacao());
		ati.setSituacao(atividadeAluno.getSituacao());

		atiAlunoRepo.save(ati);

		return "redirect:/areaadministrativanotas/" + ati.getMateria().getId();
	}

	@RequestMapping("/salvarnotaedicaogrupo")
	public String salvarNotasGrupo(@Valid AtividadeAluno atividadeAluno) {
	
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
		
		if(ati.getGrupo()==null){
			ati.setNota(atividadeAluno.getNota());
			ati.setAnotacao(atividadeAluno.getAnotacao());
			ati.setSituacao(atividadeAluno.getSituacao());
			atiAlunoRepo.save(ati);
		}

		return "redirect:/listarporgrupos/" + atividade.getId();
	}

	@GetMapping("/voltarareaadministrativanotas/{id}")
	public String voltar(@PathVariable("id") Integer id) {

		AtividadeAluno ati = atiAlunoRepo.getOne(id);

		return "redirect:/areaadministrativanotas/" + ati.getMateria().getId();
	}

	@GetMapping("/areaadministrativanotas/{id}")
	public String areaAdministrativaNotas(@PathVariable("id") Integer id, ModelMap model) {

		Materia materia = materiaRepo.getOne(id);
		Professor professor = repo.getOne(materia.getCurso().getProfessor().getId());

		List<AtividadeAluno> atividades =  atiAlunoRepo.findAtividades(materia.getId());
		List<Aluno> aluno = materia.getAlunos();

		for (AtividadeAluno c : atividades) {

			if (c.getAlunoProfessor() != null) {

				Aluno alu = new Aluno(null, c.getAlunoProfessor().getNome(), null, null, c.getAlunoProfessor().getRgm(),
						null, null);
				c.setAluno(alu);
			}
		}

		model.addAttribute("quantidade", aluno.size());
		model.addAttribute("materia", materia);
		model.addAttribute("atividade", atividades);
		model.addAttribute("professor", professor);

		return "/professor/listaadministranotas";
	}

	@GetMapping("/liberanota/{id}")
	public String liberaNota(@PathVariable("id") Integer id, ModelMap model) {

		Materia materia = materiaRepo.getOne(id);
		Boolean a = materia.getLiberarNota();
		if (a) {
			materia.setLiberarNota(false);
			materiaRepo.save(materia);
		} else {
			materia.setLiberarNota(true);
			materiaRepo.save(materia);
		}
		return "redirect:/areaadministrativanotas/" + materia.getId();
	}

	@GetMapping("/professor/preparalistaaluno/{id}")
	public String preparaListaAluno(@PathVariable("id") Integer id, ModelMap model) {

		Curso curso = cursoRepo.getOne(id);

		model.addAttribute("curso", curso);
		return "/professor/listarmateriasalunos";
	}

	@RequestMapping("/professor/salvaredicao")
	public String editarProfessor(@Valid Professor professor, BindingResult result, RedirectAttributes attr,
			ModelMap model) {

		try {

			if (result.hasErrors()) {

				attr.addFlashAttribute("messages", "Emails já cadastrados no sistema não são permitidos");
				model.addAttribute("professor", professor);
				return "/professor/editar_professor";
			}

			Professor pro = repo.getOne(professor.getId());
			pro.setEmail(professor.getEmail());
			pro.setNome(professor.getNome());
			pro.setSenha(professor.getSenha());
			repo.save(pro);
			attr.addFlashAttribute("message", "Professor atualizado com sucesso!");

			model.addAttribute("professor", pro);

			return "/professor/professor_principal";

		} catch (Exception e) {
			attr.addFlashAttribute("messages", "Emails já cadastrados no sistema não são permitidos");
			model.addAttribute("professor", professor);
			return "/professor/editar_professor";
		}
	}

	@GetMapping("professor/principal/{id}")
	public String telaPrincipal(@PathVariable("id") Integer id, ModelMap model) {

		Professor professor = repo.getOne(id);

		model.addAttribute("professor", professor);

		return "/professor/professor_principal";
	}

	@RequestMapping("/professor/editarfoto")
	public String editarFoto(@RequestParam("file") MultipartFile file, @Valid Professor professor,
			RedirectAttributes attr, ModelMap model) {

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
			model.addAttribute("professor", pro);
			return "/professor/editar_professor";
		} catch (Exception e) {
			attr.addFlashAttribute("messages", "Erro. foto nao alterada!");
			model.addAttribute("professor", pro);
			return "/professor/editar_professor";
		}

	}

	@GetMapping("/trazeralunomateria/{id}")
	public String trazerAlunosMateria(@PathVariable("id") Integer id, ModelMap model) {

		Materia materia = materiaRepo.getOne(id);
		Professor professor = repo.getOne(materia.getCurso().getProfessor().getId());

		List<Aluno> alunos = materia.getAlunos();

		model.addAttribute("alunos", alunos);
		model.addAttribute("professor", professor);

		return "/professor/listaralunos";
	}
}