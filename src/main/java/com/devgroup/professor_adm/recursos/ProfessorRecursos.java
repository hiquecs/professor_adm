package com.devgroup.professor_adm.recursos;

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

import com.devgroup.professor_adm.Repositorios.CursoRepository;
import com.devgroup.professor_adm.Repositorios.ProfessorRepository;
import com.devgroup.professor_adm.dominio.Curso;
import com.devgroup.professor_adm.dominio.Professor;
import com.devgroup.professor_adm.servicos.S3Service;

@Controller
public class ProfessorRecursos {

    @Autowired
	private ProfessorRepository repo;
    
    @Autowired
   	private CursoRepository cursoRepo;
    
    @Autowired
	private S3Service salva;
	

	@GetMapping("professor/editar/{id}")
	public String preEditar(@PathVariable("id") Integer id, ModelMap model) {

		// Obtendo o registro do professor a ser editado
		Professor professor = repo.getOne(id);

		model.addAttribute("professor", professor);
		return "/professor/editar_professor";
	}
	
	@GetMapping("/professor/preparalistaaluno/{id}")
	public String preparaListaAluno(@PathVariable("id") Integer id, ModelMap model) {

		
		Curso curso = cursoRepo.getOne(id);

		model.addAttribute("curso",curso);
		return "/professor/listarmateriasalunos";
	}
	
	@RequestMapping("/professor/salvaredicao")
	public String editarProfessor(@Valid Professor professor, BindingResult result, RedirectAttributes attr,ModelMap model) {

		try {
			
			if (result.hasErrors()) {
				
				attr.addFlashAttribute("messages", "Emails já cadastrados no sistema não são permitidos");
				model.addAttribute("professor", professor);
				return "/professor/editar_professor";
			}
			
			repo.save(professor);
			attr.addFlashAttribute("message", "Professor atualizado com sucesso!");
			
			
			model.addAttribute("professor", professor);
			
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
	public String editarFoto(@RequestParam("file") MultipartFile file, @Valid Professor professor,RedirectAttributes attr,ModelMap model) {
		
		     Professor pro = repo.getOne(professor.getId());
		try {
		    String a = ""+salva.uploadFile(file);
		    pro.setUrl(a);
			repo.save(pro);
			attr.addFlashAttribute("message", "Foto alterada com sucesso!");
			model.addAttribute("professor", pro);
			return  "/professor/editar_professor";
		}catch(Exception e) {
			attr.addFlashAttribute("messages", "Erro. foto nao alterada!");
			model.addAttribute("professor", pro);
			return  "/professor/editar_professor";
		}
		
	}
	
	
}
