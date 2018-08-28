package com.bolsadeideas.springboot.app.models.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
												//primero por la entidad y el segundo por la llave primaria Id =long	
public interface IClienteDao extends CrudRepository<Cliente, Long> {
	
		
}
