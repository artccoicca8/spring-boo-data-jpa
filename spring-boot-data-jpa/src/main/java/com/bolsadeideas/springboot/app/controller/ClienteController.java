package com.bolsadeideas.springboot.app.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bolsadeideas.springboot.app.models.dao.IClienteDao;
import com.bolsadeideas.springboot.app.models.entity.Cliente;

@Controller
public class ClienteController {

	@Autowired
	@Qualifier("ClienteDaoJPA")
	private IClienteDao clienteDao; 
	
	@RequestMapping(value="/listar",method=RequestMethod.GET)
	public String listar (Model model ) {
		
		
		model.addAttribute("titulo","listado de clientes ");
		model.addAttribute("clientes", clienteDao.findAll()); 
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
	public String wardar(@Valid Cliente cliente ,BindingResult result, Model model ) {
		
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario Cliente"); 
			return "form";
		}
		clienteDao.save(cliente);
		return "redirect:listar"; 
	}
	
}
