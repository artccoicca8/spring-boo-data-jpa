package com.bolsadeideas.springboot.app.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.service.IClienteService;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;

@Controller
@SessionAttributes("cliente") // permite setear el objeto cliente en la session
public class ClienteController {

	private final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

	@Autowired
	private IClienteService clienteService;

	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Cliente cliente = clienteService.findOne(id);

		if (cliente == null) {
			flash.addFlashAttribute("error", "ID Cliente no existe en bd ");
			return "redirect:listar";

		}
		model.put("titulo", "Detalle Cliente");
		model.put("cliente", cliente);
		return "ver";
	}

	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {

		// este el el pagina , indica el numero y la cantidad de columnas de 5 en 5
		Pageable pageable = new PageRequest(page, 5);

		Page<Cliente> clientes = clienteService.findAll(pageable);
		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);
		model.addAttribute("titulo", "listado de clientes ");
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		return "listar";

	}

	@RequestMapping(value = "/form")
	public String create(Map<String, Object> model) {

		Cliente cliente = new Cliente();
		model.put("titulo", "Formulario Cliente");
		model.put("cliente", cliente);
		return "form";
	}

	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String wardar(@Valid Cliente cliente, BindingResult result, Model model,
			@RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {

		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario Cliente");
			return "form";
		}

		if (!foto.isEmpty()) {
			/**
			 * Path directorioRecursos = Paths.get("src//main//resources//static//uploads");
			 **/
			/**
			 * Directorio fisica en donde se almacenan los archivos que va a amenjar las
			 * aplicaciones
			 *
			 * **/
			 String rootPath = "D://ImagenesSpringMvc5";
			 
			/**
			 * Se genera un nombre unico para la imagen subida 
			 * */	
			String uniqueFileName = UUID.randomUUID().toString()+"_"+foto.getOriginalFilename();
			
	
			try {
				byte[] bytes = foto.getBytes();
				Path rutaCompleta = Paths.get(rootPath + "//" + uniqueFileName);
				Files.write(rutaCompleta, bytes);
				flash.addFlashAttribute("info", "Has subido corectamente '" + uniqueFileName + "'");
				cliente.setFoto(uniqueFileName);
			} catch (IOException e) {
				e.printStackTrace();

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		String msgFlash = (cliente.getId() == null) ? "Cliente Creado con éxito " : " Cliente Editado con Exito ";

		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success", msgFlash);
		return "redirect:/listar";
	}

	@RequestMapping(value = "/form/{id}")
	public String edit(@PathVariable(value = "id") Long id, RedirectAttributes flash, Map<String, Object> model) {

		Cliente cliente = null;
		if (id > 0) {
			cliente = clienteService.findOne(id);
			if (cliente == null) {
				flash.addFlashAttribute("error", "ID Cliente no existe en bd ");
				return "redirect:listar";
			}
		} else {
			flash.addFlashAttribute("error", "ID Cliente no 0 ");
			return "redirect:listar";
		}
		model.put("titulo", "Formulario Cliente");
		model.put("cliente", cliente);
		return "form";
	}

	@RequestMapping(value = "/eliminar/{id}")
	public String delete(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

		if (id > 0) {
			clienteService.delete(id);
			flash.addFlashAttribute("success", "Cliente eliminado con exito ");
		}
		return "redirect:/listar";
	}

}
