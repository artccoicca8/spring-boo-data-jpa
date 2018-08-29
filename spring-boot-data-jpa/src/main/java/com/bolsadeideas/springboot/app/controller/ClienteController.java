package com.bolsadeideas.springboot.app.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.service.IClienteService;

@Controller
@SessionAttributes("cliente") // permite setear el objeto cliente en la session 
public class ClienteController {

	@Autowired
	private IClienteService clienteService; 
	
	@RequestMapping(value="/listar",method=RequestMethod.GET)
	public String listar ( @RequestParam(name="page", defaultValue="0") int page, Model model ) {
		
		// este el el pagina , indica el numero y la cantidad de columnas de 5 en 5 
		Pageable pageable = new PageRequest(page, 5); 
		
		Page<Cliente> clientes = clienteService.findAll(pageable);
		model.addAttribute("titulo","listado de clientes ");
		model.addAttribute("clientes", clientes); 
		return "listar";
		
	
	}
	
	@RequestMapping(value="/form")
	public String create(Map<String,Object> model) {
		
		Cliente cliente = new Cliente(); 
		model.put("titulo", "Formulario Cliente");
		model.put("cliente", cliente);
		return "form"; 
	}
	
	@RequestMapping(value="/form",method=RequestMethod.POST)
	public String wardar(@Valid Cliente cliente ,BindingResult result, Model model ,RedirectAttributes flash, SessionStatus status) {
		
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario Cliente"); 
			return "form";
		}
		
		
		String msgFlash = (cliente.getId()==null )? "Cliente Creado con éxito ":" Cliente Editado con Exito ";
		
		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", msgFlash);
		return "redirect:/listar"; 
	}
	
	
	@RequestMapping(value="/form/{id}")
	public String edit(@PathVariable(value="id") Long id,RedirectAttributes flash,  Map<String,Object> model ) {
		
		Cliente cliente = null;
		if (id>0) {
			cliente=clienteService.findOne(id);
			if (cliente==null) {
				flash.addFlashAttribute("error", "ID Cliente no existe en bd ");
				return "redirect:listar"; 
			}
		}else {
			flash.addFlashAttribute("error", "ID Cliente no 0 ");
			return "redirect:listar"; 
		}
		model.put("titulo", "Formulario Cliente");
		model.put("cliente", cliente);
		return "form"; 
	}
	
	
	@RequestMapping(value="/eliminar/{id}")
	public String delete(@PathVariable(value="id") Long id,RedirectAttributes flash) {
		
		if (id>0) {
			clienteService.delete(id);
			flash.addFlashAttribute("success", "Cliente eliminado con exito ");
		}
		return "redirect:/listar"; 
	}
	
}
