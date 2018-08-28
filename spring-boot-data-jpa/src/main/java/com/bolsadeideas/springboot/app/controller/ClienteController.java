package com.bolsadeideas.springboot.app.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.service.IClienteService;

@Controller
@SessionAttributes("cliente") // permite setear el objeto cliente en la session 
public class ClienteController {

	@Autowired
	private IClienteService clienteService; 
	
	@RequestMapping(value="/listar",method=RequestMethod.GET)
	public String listar (Model model ) {
		
		
		model.addAttribute("titulo","listado de clientes ");
		model.addAttribute("clientes", clienteService.findAll()); 
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
	public String wardar(@Valid Cliente cliente ,BindingResult result, Model model , SessionStatus status) {
		
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario Cliente"); 
			return "form";
		}
		clienteService.save(cliente);
		status.setComplete();
		return "redirect:/listar"; 
	}
	
	
	@RequestMapping(value="/form/{id}")
	public String edit(@PathVariable(value="id") Long id,  Map<String,Object> model ) {
		
		Cliente cliente = null;
		if (id>0) {
			cliente=clienteService.findOne(id);
		}else {
			return "redirect:listar"; 
		}
		model.put("titulo", "Formulario Cliente");
		model.put("cliente", cliente);
		return "form"; 
	}
	
	
	@RequestMapping(value="/eliminar/{id}")
	public String delete(@PathVariable(value="id") Long id) {
		
		if (id>0) {
			clienteService.delete(id);
		}
		return "redirect:/listar"; 
	}
	
}
